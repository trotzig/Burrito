package burrito.test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;

import burrito.BurritoRouter;
import burrito.services.FeedsSubscription;
import burrito.services.FeedsSubscriptionLowlevelQuery;
import burrito.services.FeedsSubscriptionMessage;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;


public class BroadcastTest extends TestBase {

	
	private static Map<String, String[]> params = new HashMap<String, String[]>();
	static {
		params.put("message", new String[]{"test-message"});
		params.put("secret", new String[]{"foo"});
	}
	
	
	@Test
	public void testPush() {
		FeedsSubscription sub1 = new FeedsSubscription();
		sub1.setChannelId("foo");
		sub1.setClientId("foo_client");
		sub1.setFeedIds(Arrays.asList("feed-x"));
		sub1.setCreated(new Date());
		sub1.insert();
		
		FeedsSubscription sub2 = new FeedsSubscription();
		sub2.setChannelId("bar");
		sub2.setClientId("bar_client");
		sub2.setFeedIds(Arrays.asList("feed-x"));
		sub2.setCreated(new Date());
		sub2.insert();
		
		FeedsSubscription sub3 = new FeedsSubscription(); //a poll subscription
		sub3.setClientId("poll_client");
		sub3.setFeedIds(Arrays.asList("feed-x"));
		sub3.setCreated(new Date());
		sub3.insert();
		
		FeedsSubscription sub4 = new FeedsSubscription(); //also a poll subscription, but for a different feed
		sub4.setClientId("poll_client");
		sub4.setFeedIds(Arrays.asList("feed-bb"));
		sub4.setCreated(new Date());
		sub4.insert();
		
		
		@SuppressWarnings("unchecked")
		Map<String, String> result = (Map<String, String>) TestUtils.runController("/burrito/feeds/feed-x/broadcast", params, BurritoRouter.class);
		Assert.assertEquals("ok", result.get("status"));	
		
		
		//broadcast another message
		TestUtils.runController("/burrito/feeds/feed-x/broadcast", params, BurritoRouter.class);
		
		List<FeedsSubscriptionMessage> delayeds = FeedsSubscriptionMessage.fetchBySubscriptionId(sub3.getId());
		Assert.assertEquals(2, delayeds.size());
		Assert.assertEquals("feed-x", delayeds.get(0).getFeedId());
		
		
		delayeds = FeedsSubscriptionMessage.fetchBySubscriptionId(sub4.getId());
		Assert.assertEquals(0, delayeds.size());
		
		
		
	}
	
	@Test
	public void testPushAsync() {
		//Run an async broadcast
		
		FeedsSubscription sub1 = new FeedsSubscription();
		sub1.setChannelId("foo");
		sub1.setClientId("foo_client");
		sub1.setFeedIds(Arrays.asList("feed-x"));
		sub1.setCreated(new Date());
		sub1.insert();
		
		FeedsSubscription sub2 = new FeedsSubscription();
		sub2.setChannelId("bar");
		sub2.setClientId("bar_client");
		sub2.setFeedIds(Arrays.asList("feed-x"));
		sub2.setCreated(new Date());
		sub2.insert();
		
		
		
		@SuppressWarnings("unchecked")
		Map<String, String> result = (Map<String, String>) TestUtils.runController("/burrito/feeds/feed-x/broadcast/async", params, BurritoRouter.class);
		Assert.assertEquals("ok", result.get("status"));	
		
		LocalTaskQueue ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();
		QueueStateInfo qsi = ltq.getQueueStateInfo().get(QueueFactory.getQueue("burrito-broadcast").getQueueName());
        Assert.assertEquals(1, qsi.getTaskInfo().size());        
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testThousandsOfSubscribers() {
		
		//create 2345 subscribers with a 90% overweight towards push
		createSubscribers(2345, 0.9, "feed-x");
		//also create subscribers for a completely different thread:
		createSubscribers(400, 0.8, "feed-y");
		
		Map<String, String> result = (Map<String, String>) TestUtils.runController("/burrito/feeds/feed-x/broadcast", params, BurritoRouter.class);
		Assert.assertEquals("ok", result.get("status"));
		int numberOfPolls = Integer.parseInt(result.get("numberOfPoll"));
		
		
		List<FeedsSubscriptionMessage> pollMessages = FeedsSubscriptionMessage.all().fetch();
		Assert.assertEquals(numberOfPolls, pollMessages.size());
		
		//Check number of subscribers 
		int count = countSubscribers("feed-x");
		Assert.assertEquals(2345, count);
	}
	
	@Test
	public void testThirtyThousandPollers() {
		
		createSubscribers(5000, 0, "feed-only-poll");
		@SuppressWarnings("unchecked")
		Map<String, String> result = (Map<String, String>) TestUtils.runController("/burrito/feeds/feed-only-poll/broadcast", params, BurritoRouter.class);
		Assert.assertEquals("ok", result.get("status"));
		
		//Check stored poll messages
		List<FeedsSubscriptionMessage> pollMessages = FeedsSubscriptionMessage.all().fetch();
		Assert.assertEquals(5000, pollMessages.size());
		
		//Check number of subscribers 
		int count = countSubscribers("feed-only-poll");
		Assert.assertEquals(5000, count);
	}

	private int countSubscribers(String feedId) {
		Iterable<FeedsSubscription> feedsSubscriptions = FeedsSubscription.getSubscriptionsForFeed(feedId);
		int count = 0;
		for (@SuppressWarnings("unused") FeedsSubscription fs : feedsSubscriptions) {
			count++;
		}
		return count;
	}
	@Test
	public void testNoSubscribers() {
		
		//It should always be safe to run the controller without subscribers
		TestUtils.runController("/burrito/feeds/feed-x/broadcast", params, BurritoRouter.class);
		
		int count = countSubscribers("feed-x");
		Assert.assertEquals(0, count);
	}
	

	private void createSubscribers(int numberOfSubscribers, double pushProbability, String feedId) {
		createSubscribers(numberOfSubscribers, pushProbability, feedId, new Date());
	}
	
	private void createSubscribers(int numberOfSubscribers, double pushProbability, String feedId, Date created) {
		for (int i = 0; i < numberOfSubscribers; i++) {
			FeedsSubscription sub = new FeedsSubscription();
			if (Math.random() < pushProbability) {
				sub.requestChannel();
			}
			sub.setFeedIds(Arrays.asList(feedId, "other-feed-1", "other-feed-2", "other-feed-3"));
			sub.setCreated(created);
			sub.insert();
		}
	}
	
	
	
	@Test
	public void testConcurrentKeepaliveWhilePushing() throws InterruptedException {

		// this test case will make sure that each FeedsSubscription is
		// processed only once during a broadcast. 
		
		String feed = "feed-x";
		
		createSubscribers(450, 0.95, feed);
		final List<FeedsSubscription> aCoupleOfSubs = FeedsSubscription.all()
				.filter("feedIds", feed).fetch(80);

		final AtomicInteger count = new AtomicInteger(0);
		
		FeedsSubscriptionLowlevelQuery query = new FeedsSubscriptionLowlevelQuery() {
			@Override
			protected void onNewBatch(Iterator<Entity> currentIterator) {
				//Reset timestamps for a couple of feeds
				for (FeedsSubscription fs : aCoupleOfSubs) {
					fs.keepAlive();
					fs.update();
				}
			}
			@Override
			public FeedsSubscription next() {
				FeedsSubscription sub = super.next();
				if (sub != null) {
					count.incrementAndGet();
				}
				return sub;
			}
		};
		for (@SuppressWarnings("unused") FeedsSubscription fs : FeedsSubscription.getSubscriptionsForFeed(feed, query)) {
			//do nothing, this is just for iterating through the entire result set
		}
		// asert that the number of total messages sent are the same as the
		// number of subscribers we earlier created
		Assert.assertEquals(450, count.get());

	}	
	
	
	/**
	 * Checks if inactive subscriptions are really inactivated
	 */
	public void testInactivate() {
		
		//create 30 active subscribers
		createSubscribers(30, 0.9, "feed-x");
		
		//create 20 active subscribers 6 minutes ago 
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -6);
		createSubscribers(20, 0.9, "feed-x", cal.getTime());
		
		TestUtils.runController("/burrito/feeds/feed-x/broadcast", params, BurritoRouter.class);
		int count = countSubscribers("feed-x");
		
		//should be 50 during first round, since the inactivate job hasn't been run
		Assert.assertEquals(50, count);
		
		//run inactivation job
		TestUtils.runController("/burrito/feeds/inactivate", params, BurritoRouter.class);

		//broadcast once more
		TestUtils.runController("/burrito/feeds/feed-x/broadcast", params, BurritoRouter.class);
		count = countSubscribers("feed-x");
		
		//should be 30 after inactivation job has been run
		Assert.assertEquals(30, count);
		
		
		//reactivate all subscriptions
		List<FeedsSubscription> all = FeedsSubscription.all().fetch();
		for (FeedsSubscription fs : all) {
			fs.keepAlive();
			fs.update();
		}
		
		//broadcast
		TestUtils.runController("/burrito/feeds/feed-x/broadcast", params, BurritoRouter.class);
		count = countSubscribers("feed-x");
		
		//should be 50 again, since all have been kept alive
		Assert.assertEquals(50, count);
		
		
	}
	
	
}

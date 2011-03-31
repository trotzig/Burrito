package burrito.test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import burrito.BurritoRouter;
import burrito.services.FeedsSubscription;
import burrito.services.FeedsSubscriptionMessage;

import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;


public class BroadcastTest extends TestBase {

	
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
		
		
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("message", new String[]{"test-message"});
		params.put("secret", new String[]{"foo"});
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
		
		
		
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("message", new String[]{"test-message"});
		params.put("secret", new String[]{"foo"});
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
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("message", new String[]{"test-message"});
		params.put("secret", new String[]{"foo"});
		
		createSubscribers(2345, 0.9, "feed-x");
		Map<String, String> result = (Map<String, String>) TestUtils.runController("/burrito/feeds/feed-x/broadcast", params, BurritoRouter.class);
		Assert.assertEquals("ok", result.get("status"));
		int numberOfPolls = Integer.parseInt(result.get("numberOfPoll"));
		
		
		List<FeedsSubscriptionMessage> pollMessages = FeedsSubscriptionMessage.all().fetch();
		Assert.assertEquals(numberOfPolls, pollMessages.size());
		
		//Check number of subscribers 
		Iterable<FeedsSubscription> feedsSubscriptions = FeedsSubscription.getSubscriptionsForFeed("feed-x");
		int count = 0;
		for (@SuppressWarnings("unused") FeedsSubscription fs : feedsSubscriptions) {
			count++;
		}
		Assert.assertEquals(2345, count);
	}
	
	@Test
	public void testThirtyThousandPollers() {
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("message", new String[]{"test-message"});
		params.put("secret", new String[]{"foo"});
		
		createSubscribers(5000, 0, "feed-only-poll");
		@SuppressWarnings("unchecked")
		Map<String, String> result = (Map<String, String>) TestUtils.runController("/burrito/feeds/feed-only-poll/broadcast", params, BurritoRouter.class);
		Assert.assertEquals("ok", result.get("status"));
		
		//Check stored poll messages
		List<FeedsSubscriptionMessage> pollMessages = FeedsSubscriptionMessage.all().fetch();
		Assert.assertEquals(5000, pollMessages.size());
		
		//Check number of subscribers 
		Iterable<FeedsSubscription> feedsSubscriptions = FeedsSubscription.getSubscriptionsForFeed("feed-only-poll");
		int count = 0;
		for (@SuppressWarnings("unused") FeedsSubscription fs : feedsSubscriptions) {
			count++;
		}
		Assert.assertEquals(5000, count);
	}

	private void createSubscribers(int numberOfSubscribers, double pushProbability, String feedId) {
		for (int i = 0; i < numberOfSubscribers; i++) {
			FeedsSubscription sub = new FeedsSubscription();
			if (Math.random() < pushProbability) {
				sub.requestChannel();
			}
			sub.setFeedIds(Arrays.asList(feedId, "other-feed-1", "other-feed-2", "other-feed-3"));
			sub.setCreated(new Date());
			sub.insert();
		}
	}
	
	
	
	
	
}

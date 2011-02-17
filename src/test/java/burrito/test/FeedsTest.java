package burrito.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import burrito.AdminRouter;
import burrito.services.FeedsSubscription;

public class FeedsTest extends TestBase {

	@Test
	@SuppressWarnings("unchecked")
	public void testFeedsControllers() throws UnsupportedEncodingException {

		/*
		 * Create a new channel
		 */

		Object o = TestUtils.runController("/burrito/feeds/subscription/new", AdminRouter.class);

		assertTrue(o instanceof Map<?, ?>);

		Map<String, String> result = (Map<String, String>) o;

		assertEquals("ok", result.get("status"));

		String subscriptionId = result.get("subscriptionId");
		String channelId = result.get("channelId");

		assertNotNull(subscriptionId);
		assertNotNull(channelId);

		FeedsSubscription subscription = FeedsSubscription.getById(Long.valueOf(subscriptionId));

		assertNotNull(subscription);

		String clientId = subscription.getClientId();

		assertNotNull(clientId);
		assertEquals(subscriptionId, subscription.getId().toString());
		assertEquals(channelId, subscription.getChannelId());

		List<String> feedIds = subscription.getFeedIds();

		assertNull(feedIds);

		Date created = subscription.getCreated();

		assertNotNull(created);
		assertEquals(created, subscription.getTimestamp());

		/*
		 * Add feeds to the channel
		 */

		String testFeedIds[] = { "A", "B", "C", "A" };
		Set<String> uniqueTestFeedIds = new HashSet<String>(); 
		for (String feedId : testFeedIds) {
			if (!uniqueTestFeedIds.contains(feedId)) {
				uniqueTestFeedIds.add(feedId);
			}
		}

		for (int i = 0; i < testFeedIds.length; i++)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("/burrito/feeds/subscription/");
			sb.append(subscriptionId);
			sb.append("/addFeed/");
			sb.append(URLEncoder.encode(testFeedIds[ i ], "UTF-8"));

			o = TestUtils.runController(sb.toString(), AdminRouter.class);

			assertTrue(o instanceof Map<?, ?>);

			result = (Map<String, String>) o;

			assertEquals("ok", result.get("status"));

			subscription = FeedsSubscription.getById(Long.valueOf(subscriptionId));

			assertNotNull(subscription);
			assertEquals(subscriptionId, subscription.getId().toString());
			assertEquals(clientId, subscription.getClientId());
			assertEquals(channelId, subscription.getChannelId());

			feedIds = subscription.getFeedIds();

			assertNotNull(feedIds);
			assertEquals(Math.min(i + 1, uniqueTestFeedIds.size()), feedIds.size());

			for (int j = 0; j <= i; j++)
			{
				assertTrue(feedIds.contains(testFeedIds[ j ]));
			}

			assertEquals(created, subscription.getCreated());
			assertEquals(created, subscription.getTimestamp());
		}

		/*
		 * Call keepAlive on the channel
		 */

		StringBuilder sb = new StringBuilder();
		sb.append("/burrito/feeds/subscription/");
		sb.append(subscriptionId);
		sb.append("/keepAlive");

		o = TestUtils.runController(sb.toString(), AdminRouter.class);

		assertTrue(o instanceof Map<?, ?>);

		result = (Map<String, String>) o;

		assertEquals("ok", result.get("status"));

		subscription = FeedsSubscription.getById(Long.valueOf(subscriptionId));

		assertNotNull(subscription);
		assertEquals(subscriptionId, subscription.getId().toString());
		assertEquals(clientId, subscription.getClientId());
		assertEquals(channelId, subscription.getChannelId());

		feedIds = subscription.getFeedIds();

		assertNotNull(feedIds);
		assertEquals(uniqueTestFeedIds.size(), feedIds.size());
		assertEquals(created, subscription.getCreated());

		assertTrue(created.before(subscription.getTimestamp()));

		/*
		 * Try adding a feed to a non-existing channel
		 */

		o = TestUtils.runController("/burrito/feeds/subscription/" + Long.MAX_VALUE + "/addFeed/whatever", AdminRouter.class);

		assertTrue(o instanceof Map<?, ?>);

		result = (Map<String, String>) o;

		assertEquals("error", result.get("status"));

		/*
		 * Try calling keepAlive on a non-existing channel
		 */

		o = TestUtils.runController("/burrito/feeds/subscription/" + Long.MAX_VALUE + "/keepAlive", AdminRouter.class);

		assertTrue(o instanceof Map<?, ?>);

		result = (Map<String, String>) o;

		assertEquals("error", result.get("status"));
	}
}

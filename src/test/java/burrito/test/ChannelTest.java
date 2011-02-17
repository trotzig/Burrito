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
import java.util.UUID;

import org.junit.Test;

import burrito.AdminRouter;
import burrito.services.ChannelSubscription;

public class ChannelTest extends TestBase {

	@Test
	@SuppressWarnings("unchecked")
	public void testChannelControllers() throws UnsupportedEncodingException {

		/*
		 * Create a new channel
		 */

		Object o = TestUtils.runController("/burrito/channel/new", AdminRouter.class);

		assertTrue(o instanceof Map<?, ?>);

		Map<String, String> result = (Map<String, String>) o;

		assertEquals("ok", result.get("status"));

		String channelId = result.get("channelId");

		assertNotNull(channelId);

		ChannelSubscription subscription = ChannelSubscription.getByChannelId(channelId);

		assertNotNull(subscription);

		String clientId = subscription.getClientId();

		assertNotNull(clientId);
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
			sb.append("/burrito/channel/");
			sb.append(URLEncoder.encode(channelId, "UTF-8"));
			sb.append("/addFeed/");
			sb.append(URLEncoder.encode(testFeedIds[ i ], "UTF-8"));

			o = TestUtils.runController(sb.toString(), AdminRouter.class);

			assertTrue(o instanceof Map<?, ?>);

			result = (Map<String, String>) o;

			assertEquals("ok", result.get("status"));

			subscription = ChannelSubscription.getByChannelId(channelId);

			assertNotNull(subscription);
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
		sb.append("/burrito/channel/");
		sb.append(URLEncoder.encode(channelId, "UTF-8"));
		sb.append("/keepAlive");

		o = TestUtils.runController(sb.toString(), AdminRouter.class);

		assertTrue(o instanceof Map<?, ?>);

		result = (Map<String, String>) o;

		assertEquals("ok", result.get("status"));

		subscription = ChannelSubscription.getByChannelId(channelId);

		assertNotNull(subscription);
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

		o = TestUtils.runController("/burrito/channel/" + UUID.randomUUID().toString() + "/addFeed/nonsense", AdminRouter.class);

		assertTrue(o instanceof Map<?, ?>);

		result = (Map<String, String>) o;

		assertEquals("error", result.get("status"));

		/*
		 * Try calling keepAlive on a non-existing channel
		 */

		o = TestUtils.runController("/burrito/channel/" + UUID.randomUUID().toString() + "/keepAlive", AdminRouter.class);

		assertTrue(o instanceof Map<?, ?>);

		result = (Map<String, String>) o;

		assertEquals("error", result.get("status"));
	}
}

package burrito.test;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Map;

import org.junit.Test;

import burrito.BurritoRouter;
import burrito.services.FeedsSubscription;
import burrito.services.FeedsSubscriptionMessage;

public class FeedsCleanupTest extends TestBase {

	@Test
	public void testCleanup() {
		insertEntities(-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2);

		@SuppressWarnings("unchecked")
		Map<String, String> result = (Map<String, String>) TestUtils
				.runController("/burrito/feeds/cleanup/5", BurritoRouter.class);
		assertNotNull(result.get("success"));
		assertEquals("12 old feeds removed.", result.get("success"));
	}

	private void insertEntities(Integer... dayOffsets) {
		for (int offset : dayOffsets) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, offset);

			FeedsSubscription fs = new FeedsSubscription();
			fs.setCreated(cal.getTime());
			fs.setTimestamp(cal.getTime());
			fs.insert();

			FeedsSubscriptionMessage fsm = new FeedsSubscriptionMessage();
			fsm.setTimestamp(cal.getTime());
			fsm.insert();
		}
	}
}

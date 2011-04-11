/**
 * Copyright 2011 Henric Persson (henric.persson@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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

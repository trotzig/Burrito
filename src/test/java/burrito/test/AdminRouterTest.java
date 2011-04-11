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

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import burrito.BurritoRouter;
import burrito.services.SearchEntry;


public class AdminRouterTest extends TestBase {
	
	@Test
	public void testCrudMessages() {
		Object o = TestUtils.runController("/burrito/crudmessages.js", BurritoRouter.class);

		assertTrue(o instanceof Map<?, ?>);
		
		o = TestUtils.runController("/asdasd", BurritoRouter.class);
		assertNull(o);
		
		
//		o = TestUtils.runController("/asdasd", AdminRouter.class);
		
		
		SearchEntry s = new SearchEntry();
		s.ownerId = 234234L;
		s.insert();
		
	}
	
	
}

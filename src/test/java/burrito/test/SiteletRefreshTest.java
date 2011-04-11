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

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import burrito.BurritoRouter;
import burrito.services.SiteletProperties;


public class SiteletRefreshTest extends TestBase {

	
	@Test
	public void testSiteletRefresh() {
		
		SiteletProperties props = new SiteletProperties();
		props.insert();
		
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("siteletPropertiesId", new String[]{String.valueOf(props.getId())});
		SiteletProperties p = (SiteletProperties) TestUtils.runController("/burrito/sitelets/refresh/sitelet", params, BurritoRouter.class);
		Assert.assertEquals(props.getId(), p.getId());
	}
	
	
}

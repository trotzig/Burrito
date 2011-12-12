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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import burrito.BurritoRouter;
import burrito.Configurator;
import burrito.services.SiteletProperties;
import burrito.util.SiteletHelper;

public class SiteletRefreshTest extends TestBase {

	private boolean realMayRetireSitelets;

	@Override
	public void setUp() {
		super.setUp();

		realMayRetireSitelets = Configurator.MAY_RETIRE_SITELETS;
		Configurator.MAY_RETIRE_SITELETS = true;
	}

	@Override
	public void tearDown() {
		super.tearDown();

		Configurator.MAY_RETIRE_SITELETS = realMayRetireSitelets;
	}

	@Test
	public void testSiteletRefresh() {
		
		SiteletProperties props = new SiteletProperties();
		props.insert();
		
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("siteletPropertiesId", new String[]{String.valueOf(props.getId())});
		SiteletProperties p = (SiteletProperties) TestUtils.runController("/burrito/sitelets/refresh/sitelet", params, BurritoRouter.class);
		Assert.assertEquals(props.getId(), p.getId());
	}

	@Test
	public void testRefreshSiteletsController() {
		SiteletProperties props1 = createSiteletProperties(-1L, true);
		SiteletProperties props2 = createSiteletProperties(-1L, false);
		SiteletProperties props3 = createSiteletProperties(5 * 60 * 1000L, true);
		SiteletProperties props4 = createSiteletProperties(5 * 60 * 1000L, false);
		SiteletProperties props5 = createSiteletProperties(null, true);
		SiteletProperties props6 = createSiteletProperties(null, false);

		TestUtils.runController("/burrito/sitelets/refresh", null, BurritoRouter.class);

		props1 = SiteletProperties.get(props1.getId());
		props2 = SiteletProperties.get(props2.getId());
		props3 = SiteletProperties.get(props3.getId());
		props4 = SiteletProperties.get(props4.getId());
		props5 = SiteletProperties.get(props5.getId());
		props6 = SiteletProperties.get(props6.getId());

		Assert.assertEquals(false, props1.isRetired());
		Assert.assertNotNull(props1.nextAutoRefresh);

		Assert.assertEquals(true, props2.isRetired());
		Assert.assertNotNull(props2.nextAutoRefresh);
		Assert.assertEquals(9999999999999L, props2.nextAutoRefresh.getTime());

		Assert.assertEquals(false, props3.isRetired());
		Assert.assertNotNull(props3.nextAutoRefresh);

		Assert.assertEquals(false, props4.isRetired());
		Assert.assertNotNull(props4.nextAutoRefresh);

		Assert.assertEquals(false, props5.isRetired());
		Assert.assertNotNull(props5.nextAutoRefresh);
		Assert.assertEquals(9999999999999L, props5.nextAutoRefresh.getTime());

		Assert.assertEquals(false, props6.isRetired());
		Assert.assertNotNull(props6.nextAutoRefresh);
		Assert.assertEquals(9999999999999L, props6.nextAutoRefresh.getTime());
	}

	private SiteletProperties createSiteletProperties(Long refreshDelta, boolean hasBeenDisplayed) {
		SiteletProperties props = new SiteletProperties();

		if (refreshDelta != null) {
			props.setNextAutoRefresh(new Date(System.currentTimeMillis() + refreshDelta));
		}
		else {
			props.setNextAutoRefreshToFarIntoTheFuture();
		}

		props.insert();

		if (hasBeenDisplayed) {
			SiteletHelper.touchSiteletLastDisplayTime(props.getId());
		}

		return props;
	}
}

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

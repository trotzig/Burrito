package burrito.test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import burrito.AdminRouter;
import burrito.services.SearchEntry;


public class AdminRouterTest extends TestBase {
	
	@Test
	public void testCrudMessages() {
		Object o = TestUtils.runController("/burrito/crudmessages.js", AdminRouter.class);

		assertTrue(o instanceof Map<?, ?>);
		
		o = TestUtils.runController("/asdasd", AdminRouter.class);
		assertNull(o);
		
		
//		o = TestUtils.runController("/asdasd", AdminRouter.class);
		
		
		SearchEntry s = new SearchEntry();
		s.ownerId = 234234L;
		s.insert();
		
	}
	
	
}

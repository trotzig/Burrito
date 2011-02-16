package burrito.test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

import burrito.AdminRouter;
import burrito.services.SearchEntry;


public class AdminRouterTest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig(),
			new LocalMemcacheServiceTestConfig(),
			new LocalTaskQueueTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}
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

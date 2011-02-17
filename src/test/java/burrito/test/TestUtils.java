package burrito.test;

import java.util.HashMap;
import java.util.Map;

import taco.PreparedFlow;
import taco.Router;

public class TestUtils {

	public static Object runController(String uri, Map<String, String[]> requestParams, Class<? extends Router> router) {
		Router r;
		try {
			r = router.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to create router", e);
		}
		r.init();
		PreparedFlow flow = r.execute(uri, requestParams);
		if (flow == null) {
			return null;
		}
		return flow.getContinuation().getController().execute();
		
		
	}
	
	
	public static Object runController(String uri, Class<? extends Router> router) {
		return runController(uri, new HashMap<String, String[]>(), router);
	}
	

}

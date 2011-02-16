package burrito.test;

import taco.PreparedFlow;
import taco.Router;

public class TestUtils {

	public static Object runController(String uri, Class<? extends Router> router) {
		Router r;
		try {
			r = router.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to create router", e);
		}
		r.init();
		PreparedFlow flow = r.execute(uri);
		if (flow == null) {
			return null;
		}
		return flow.getContinuation().getController().execute();
		
		
	}

}

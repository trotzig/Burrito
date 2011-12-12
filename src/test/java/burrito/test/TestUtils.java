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

		if (requestParams == null) {
			requestParams = new HashMap<String, String[]>();
		}

		MockControllerRequest request = new MockControllerRequest(uri, requestParams);
		//request.getParameterMap()
		PreparedFlow flow = r.execute(request);
		if (flow == null) {
			return null;
		}
		return flow.getContinuation().getController().execute();
		
		
	}
	
	
	public static Object runController(String uri, Class<? extends Router> router) {
		return runController(uri, new HashMap<String, String[]>(), router);
	}
	

}

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

package burrito;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * An extended version of the default taco filter. Basically, just adds support
 * for signaling that a user is admin
 * 
 * @author henper
 * 
 */
public class RouterFilter extends taco.RouterFilter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		boolean admin = Configurator.getAdminProtector().allow((HttpServletRequest) req);
		
		req.setAttribute("burrito_userIsAdmin", admin);

		//for backwards compability...:
		req.setAttribute("taco_userIsAdmin", admin);
		
		super.doFilter(req, resp, chain);
	}
	
}

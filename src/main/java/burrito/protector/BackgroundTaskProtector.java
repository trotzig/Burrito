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

package burrito.protector;

import javax.servlet.http.HttpServletRequest;

import burrito.Configurator;

import taco.Protector;

/**
 * Protector for background tasks (cron, task queue)
 * 
 * @author henper
 * 
 */
public class BackgroundTaskProtector implements Protector {

	@Override
	public boolean allow(HttpServletRequest request) {
		boolean admin = Configurator.getAdminProtector().allow(request);
		if (admin) {
			return true;
		}
		String queue = request.getHeader("X-AppEngine-QueueName");
		if (queue != null) {
			return true;
		}
		String cron = request.getHeader("X-AppEngine-Cron");
		if (cron != null) {
			return true;
		}
		// neither admin, cron or task queue
		return false;
	}

}

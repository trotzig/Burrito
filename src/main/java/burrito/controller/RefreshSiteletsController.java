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

package burrito.controller;

import java.util.List;

import burrito.Configurator;
import burrito.services.SiteletProperties;
import burrito.util.Cache;
import burrito.util.SiteletHelper;

/**
 * Iterates through all sitelets needing a refresh and adds a background job
 * that refreshes them
 * 
 * @author henper
 * 
 */
public class RefreshSiteletsController extends VoidController {

	@Override
	public Void execute() {
		List<SiteletProperties> props = SiteletProperties
				.getSiteletsNeedingRefresh();
		long yesterday = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
		for (SiteletProperties prop : props) {
			if (Configurator.MAY_RETIRE_SITELETS) {
				String cacheKey = "burrito:sitelet-last-display-time:" + prop.getId();
				Long lastDisplayTime = (Long) Cache
						.get(cacheKey );
				if (lastDisplayTime == null || lastDisplayTime < yesterday) {
					prop.setRetired(true);
					prop.setNextAutoRefresh(null);
					prop.update();
					SiteletHelper.clearSiteletContainerCache(prop.containerId);
				} else {
					prop.triggerRefreshAsync();
				}
			}  else {
				prop.triggerRefreshAsync();
			}
		}
		return null;
	}
}

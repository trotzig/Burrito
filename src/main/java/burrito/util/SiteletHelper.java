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

package burrito.util;

import java.util.ArrayList;
import java.util.List;

import burrito.services.SiteletProperties;


/**
 * Helper class for sitelets
 * 
 * @author henper
 * 
 */
public class SiteletHelper {

	private static final String CACHE_PREFIX = "sitelet-props-";
	private static final String SITELET_LAST_REFRESH_CACHE_PREFIX = "burrito:sitelet-last-display-time:";
	
	
	@SuppressWarnings("unchecked")
	public static List<SiteletProperties> getSiteletProperties(
			String containerId) {
		String cacheKey = CACHE_PREFIX + containerId;
		ArrayList<SiteletProperties> cached = (ArrayList<SiteletProperties>) Cache.get(cacheKey);
		if (cached != null) {
			return cached;
		}
		cached = new ArrayList<SiteletProperties>();
		for (SiteletProperties siteletProperties : SiteletProperties.getByContainerId(containerId)) {
			cached.add(siteletProperties);
		}
		//Cache for 5 minutes
		Cache.put(cacheKey, cached, 5 * 60);
		return cached;
	}

	
	public static void clearSiteletContainerCache(String containerId) {
		Cache.delete(SiteletHelper.CACHE_PREFIX + containerId);
	}
	
	public static Long getSiteletLastDisplayTime(Long siteletPropertiesId) {
		String cacheKey = SITELET_LAST_REFRESH_CACHE_PREFIX + siteletPropertiesId;
		return (Long) Cache.get(cacheKey);
	}
	
	public static void touchSiteletLastDisplayTime(Long siteletPropertiesId) {
		String cacheKey = SITELET_LAST_REFRESH_CACHE_PREFIX + siteletPropertiesId;
		Cache.put(cacheKey, System.currentTimeMillis());
	}
}

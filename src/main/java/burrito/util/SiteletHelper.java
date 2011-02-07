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

	public static final String CACHE_PREFIX = "sitelet-props-";
	
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


}

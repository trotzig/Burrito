package burrito.controller;

import java.util.List;

import burrito.services.SiteletProperties;

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
		for (SiteletProperties prop : props) {
			prop.triggerRefreshAsync();
		}
		return null;
	}

}

package burrito.controller;

import java.util.List;

import burrito.services.SiteletProperties;

/**
 * Refreshes all sitelets
 * 
 * @author henper
 * 
 */
public class RefreshAllSiteletsController extends VoidController {

	private String type;

	@Override
	public Void execute() {
		List<SiteletProperties> props;
		if (type == null) {
			props = SiteletProperties.all().fetch();
		} else {
			props = SiteletProperties.all().filter("entityTypeClassName", type)
					.fetch();
		}
		for (SiteletProperties prop : props) {
			prop.triggerRefreshAsync();
		}
		return null;
	}

}

package burrito.example;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import mvcaur.Router;

public class ExampleRouter extends Router {

	@Override
	public void init() {
		route("/").through(StartPageController.class).renderedBy("/start.jsp");
		route("/entry/{name}/{id:long}").through(BlogEntryController.class).renderedBy("/blogentry.jsp");
	}
	
	@Override
	public boolean isUserAdmin() {
		UserService service = UserServiceFactory.getUserService();
		if (!service.isUserLoggedIn()) {
			return false;
		}
		return service.isUserAdmin();
	}

}

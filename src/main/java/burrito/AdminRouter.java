package burrito;

import taco.Router;
import burrito.controller.AddFeedsSubscriptionFeedController;
import burrito.controller.AdminController;
import burrito.controller.KeepFeedsSubscriptionAliveController;
import burrito.controller.NewFeedsSubscriptionController;
import burrito.controller.VoidController;
import burrito.render.MessagesRenderer;
import burrito.render.SiteletAdminCSSRenderer;
import burrito.server.blobstore.BlobServiceImpl;
import burrito.server.blobstore.BlobStoreServlet;
import burrito.services.CrudServiceImpl;
import burrito.services.SiteletServiceImpl;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AdminRouter extends Router {

	UserService service = UserServiceFactory.getUserService();
	
	@Override
	public void init() {
		route("/burrito/admin").through(AdminController.class).renderedBy("/Admin.jsp");
		route("/burrito/crudmessages.js").through(MessagesController.class).renderedBy(new MessagesRenderer());
		route("/burrito/siteletadmin.css").through(VoidController.class).renderedBy(new SiteletAdminCSSRenderer());
		route("/burrito/crud").throughServlet(CrudServiceImpl.class);
		route("/burrito/sitelets").throughServlet(SiteletServiceImpl.class);
		route("/burrito/blobService").throughServlet(BlobServiceImpl.class);
		route("/blobstore/image").throughServlet(BlobStoreServlet.class);

		route("/burrito/feeds/subscription/new").through(NewFeedsSubscriptionController.class).renderAsJson();
		route("/burrito/feeds/subscription/{subscriptionId:long}/addFeed/{feedId}").through(AddFeedsSubscriptionFeedController.class).renderAsJson();
		route("/burrito/feeds/subscription/{subscriptionId:long}/keepAlive").through(KeepFeedsSubscriptionAliveController.class).renderAsJson();
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

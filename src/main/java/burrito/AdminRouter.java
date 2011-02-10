package burrito;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import mvcaur.Router;
import burrito.controller.AdminController;
import burrito.controller.VoidController;
import burrito.render.MessagesRenderer;
import burrito.render.SiteletAdminCSSRenderer;
import burrito.server.blobstore.BlobServiceImpl;
import burrito.server.blobstore.BlobStoreServlet;
import burrito.services.CrudServiceImpl;
import burrito.services.SiteletServiceImpl;

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

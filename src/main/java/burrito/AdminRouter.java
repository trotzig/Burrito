package burrito;

import mvcaur.Router;
import burrito.controller.AdminController;
import burrito.controller.VoidController;
import burrito.render.MessagesRenderer;
import burrito.render.SiteletAdminCSSRenderer;
import burrito.services.CrudServiceImpl;
import burrito.services.SiteletServiceImpl;

import com.kanal5.play.server.blobstore.BlobServiceImpl;
import com.kanal5.play.server.blobstore.BlobStoreServlet;

public class AdminRouter extends Router {

	@Override
	public void init() {
		route("/admin").through(AdminController.class).renderedBy("/Admin.jsp");
		route("/burrito/crudmessages.js").through(MessagesController.class).renderedBy(new MessagesRenderer());
		route("/burrito/siteletadmin.css").through(VoidController.class).renderedBy(new SiteletAdminCSSRenderer());
		route("/burrito/crud").throughServlet(CrudServiceImpl.class);
		route("/burrito/sitelets").throughServlet(SiteletServiceImpl.class);
		route("/burrito/blobService").throughServlet(BlobServiceImpl.class);
		route("/blobstore/image").throughServlet(BlobStoreServlet.class);
	}
	

}

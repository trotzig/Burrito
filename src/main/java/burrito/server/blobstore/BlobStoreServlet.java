package burrito.server.blobstore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

/**
 * Servlet that serves images from the blob store. Should only be used in places
 * where performance is not an issue. This servlet will simply redirect the
 * request to a proper serving URL from the blob store.
 * 
 * @author henper
 * 
 */
public class BlobStoreServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private ImagesService imagesService = ImagesServiceFactory.getImagesService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BlobKey blobKey = new BlobKey(req.getParameter("key"));
		String s = req.getParameter("s");
		if (s == null) {
			blobstoreService.serve(blobKey, resp);
		} else {
			String url = imagesService.getServingUrl(blobKey);
			url = url.replace("http://0.0.0.0:", "http://localhost:");
			url += "=s" + s;
			resp.sendRedirect(url);
		}
	}
}

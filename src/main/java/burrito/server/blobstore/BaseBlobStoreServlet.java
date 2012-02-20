package burrito.server.blobstore;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public abstract class BaseBlobStoreServlet extends HttpServlet {

	private BlobstoreService blobstoreService;

	protected BaseBlobStoreServlet() {
		blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	}
	
	public BaseBlobStoreServlet(BlobstoreService blobstoreService) {
		this.blobstoreService = blobstoreService;
	}

	public void serveBlobKey(BlobKey blobKey, HttpServletResponse resp) throws IOException {
		resp.setHeader("Cache-Control", "public, max-age=86400");
		blobstoreService.serve(blobKey, resp);
	}
	
}

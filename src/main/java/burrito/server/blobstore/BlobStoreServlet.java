package burrito.server.blobstore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;

@SuppressWarnings("serial")
public class BlobStoreServlet extends BaseBlobStoreServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlobKey blobKey = new BlobKey(req.getParameter("key"));
		serveBlobKey(blobKey, resp);
	}
	
}

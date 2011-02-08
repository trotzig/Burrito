package burrito.server.blobstore;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.repackaged.com.google.common.base.CharEscapers;

public class BlobStoreUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private BlobInfoFactory blobInfoStorage = new BlobInfoFactory();

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		// sends a redirect to itself, because of appengine blobstore
		// limitations.

		BlobKey key = blobs.get("file");

		String redirect = "/admin/blobstore/upload?key=" + key.getKeyString();

		String widthParam = req.getParameter("width");
		String heightParam = req.getParameter("height");

		if (widthParam != null || heightParam != null) {
			BlobInfo info = blobInfoStorage.loadBlobInfo(key);
			byte[] bytes = blobstoreService.fetchData(key, 0, info.getSize());
			Image i = ImagesServiceFactory.makeImage(bytes);

			String error = "";

			if (widthParam != null) {
				int width = Integer.parseInt(widthParam);
				if (i.getWidth() != width) {
					error = "Wrong image size. Your image is " + i.getWidth()
							+ " pixels wide. Required width: " + width + "\n";
				}
			}

			if (heightParam != null) {
				int height = Integer.parseInt(heightParam);
				if (i.getHeight() != height) {
					error += "Wrong image size. Your image is " + i.getHeight()
							+ " pixels tall. Required height: " + height;
				}
			}

			if (!error.isEmpty()) {
				redirect += "&error=" + CharEscapers.uriEscaper(false).escape(error);
			}
		}

		resp.sendRedirect(redirect);
	}

	private void sendError(HttpServletResponse resp, String string)
			throws IOException {
		resp.setStatus(404);
		resp.getWriter().print(string);
		resp.getWriter().close();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String error = req.getParameter("error");
		String key = req.getParameter("key");
		if (error != null) {
			// We would like to clean up the uploaded blob but the blobstore
			// upload flow does not support removing a blob in mid-upload
			// blobstoreService.delete(new BlobKey(key));

			sendError(resp, error);
			return;
		}

		// Send the blobstore key back to GWT
		resp.setStatus(200);
		resp.setContentType("text/plain");
		resp.getWriter().print("OK#" + key);
		resp.getWriter().close();
	}

}

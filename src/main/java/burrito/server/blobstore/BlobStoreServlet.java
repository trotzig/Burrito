/**
 * Copyright 2011 Henric Persson (henric.persson@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package burrito.server.blobstore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import burrito.util.StringUtils;

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

	private BlobstoreService blobstoreService;
	private ImagesService imagesService;

	public BlobStoreServlet() {
		blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		imagesService = ImagesServiceFactory.getImagesService();
	}
	
	public BlobStoreServlet(ImagesService imagesService2, BlobstoreService blobstoreService2) {
		imagesService = imagesService2;
		blobstoreService = blobstoreService2;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BlobKey blobKey = new BlobKey(req.getParameter("key"));
		String s = getSizeParam(req);
		if (s == null) {
			resp.setHeader("Cache-Control", "public, max-age=86400");
			blobstoreService.serve(blobKey, resp);
		} else {
			String url = imagesService.getServingUrl(blobKey);
			url = url.replace("http://0.0.0.0:", "http://localhost:");
			url += "=s" + s;
			resp.sendRedirect(url);
		}
	}

	private String getSizeParam(HttpServletRequest req) {
		String trueParam = req.getParameter("s");
		if (trueParam != null) {
			return trueParam;
		}
		
		String queryString = req.getQueryString();
		if (queryString == null || !queryString.contains("=s")) {
			return null;
		}
		
		//&=s178 => s178
		return StringUtils.substringAfter(queryString, "=s");
	}
}

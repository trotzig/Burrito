package burrito.server.blobstore;

import burrito.client.widgets.services.BlobService;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class BlobServiceImpl extends RemoteServiceServlet implements BlobService {
	private static final long serialVersionUID = 1L;

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	public String getBlobStoreUploadURL() {
		return blobstoreService.createUploadUrl("/admin/blobstore/upload");
	}
}

package burrito.client.widgets.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("blobService")
public interface BlobService extends RemoteService {

	String getBlobStoreUploadURL();
}

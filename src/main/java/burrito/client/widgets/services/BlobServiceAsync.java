package burrito.client.widgets.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BlobServiceAsync {

	void getBlobStoreUploadURL(AsyncCallback<String> asyncCallback);
}

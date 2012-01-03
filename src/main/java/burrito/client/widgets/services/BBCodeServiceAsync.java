package burrito.client.widgets.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BBCodeServiceAsync {

	void generateBBCodePreview(String html, AsyncCallback<String> asyncCallback);
	
}

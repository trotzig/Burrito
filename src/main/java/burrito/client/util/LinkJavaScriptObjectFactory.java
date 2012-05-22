package burrito.client.util;

import burrito.client.dto.LinkJavaScriptObject;

public class LinkJavaScriptObjectFactory {

	public static final native LinkJavaScriptObject fromJson(String json) /*-{
		return eval('(' + json + ')');
	}-*/;
}

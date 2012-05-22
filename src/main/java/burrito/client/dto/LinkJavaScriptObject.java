package burrito.client.dto;

import com.google.gwt.core.client.JavaScriptObject;

public class LinkJavaScriptObject extends JavaScriptObject {

	protected LinkJavaScriptObject() {
		// must be protected because of how JavaScriptObject works
	}

	public final native String getTypeClassName() /*-{
		return this.typeClassName;
	}-*/;

	public final native void setTypeClassName(String value) /*-{
		this.typeClassName = value;
	}-*/;

	public final native double getTypeId() /*-{
		return this.typeId;
	}-*/;

	public final native void setTypeId(double value) /*-{
		this.typeId = value;
	}-*/;

	public final native String getLinkText() /*-{
		return this.linkText;
	}-*/;

	public final native void setLinkText(String value) /*-{
		this.linkText = value;
	}-*/;

	public final native String getAbsoluteUrl() /*-{
		return this.absoluteUrl;
	}-*/;

	public final native void setAbsoluteUrl(String value) /*-{
		this.absoluteUrl = value;
	}-*/;
}

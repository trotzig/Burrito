package burrito.client.crud.widgets;

import com.google.gwt.core.client.JavaScriptObject;

public class LinkedEntityJsonOverlay extends JavaScriptObject {
	protected LinkedEntityJsonOverlay() {
		// default empty protected constructor, required by GWT on classes
		// implementing JavaScriptObject
	}

	public final native String getTypeClassName() /*-{ return this.typeClassName;
													}-*/;

	public final native int getTypeId() /*-{ return this.typeId;
											}-*/;

	public final native String getLinkText() /*-{ return this.linkText;
												}-*/;
	/**
	 * Gets an absolute link, or null if a normal typed link has been provided
	 * 
	 * @return
	 */
	public final native String getAbsoluteLink() /*-{ return this.absoluteUrl;
													}-*/;

}

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

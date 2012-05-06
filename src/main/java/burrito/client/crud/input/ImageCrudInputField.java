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

package burrito.client.crud.input;

import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.ImageField;
import burrito.client.widgets.blobstore.BlobStoreImageField;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("rawtypes")
public class ImageCrudInputField implements CrudInputField {

	private ImageField crudField;
	private VerticalPanel panel;
	private Image oldImage;
	private Integer requiredWidth;
	private Integer requiredHeight;
	private BlobStoreImageField blobField;
	private String value;

	public ImageCrudInputField(ImageField crudField) {
		this.crudField = crudField;

		panel = new VerticalPanel();

		oldImage = new Image();
		oldImage.setVisible(false);
		panel.add(oldImage);

		requiredWidth = crudField.getWidth();
		requiredHeight = crudField.getHeight();
		if (requiredWidth == 0) requiredWidth = null;
		if (requiredHeight == 0) requiredHeight = null;

		blobField = new BlobStoreImageField(crudField.isRequired(), requiredWidth, requiredHeight);
		panel.add(blobField);

		load(crudField.getValue());
		panel.addStyleName("k5-ImageCrudInputField");
	}

	public CrudField getCrudField() {
		crudField.setValue(getValue());
		return crudField;
	}

	public Widget getDisplayWidget() {
		return panel;
	}

	public Object getValue() {
		String blobStoreKey = blobField.getValue();
		if (blobStoreKey != null) {
			if (crudField.isUrlMode()) {
				value = "/blobstore/image?key=" + blobStoreKey;
			}
			else {
				value = blobStoreKey;
			}
		} else {
			value = blobStoreKey;
		}
		
		return value;
	}

	public void load(Object valueObject) {
		value = (String) valueObject;

		String previewUrl;
		String blobFieldValue;

		if (crudField.isUrlMode()) {
			previewUrl = value;
			blobFieldValue = null;

			if (value != null) {
				if (value.startsWith("/blobstore/image")) {
					int pos = value.indexOf("?key=");
					if (pos < 0) pos = value.indexOf("&key=");
					if (pos >= 0) {
						String blobStoreKey = value.substring(pos + 5);
						pos = blobStoreKey.indexOf('&');
						if (pos >= 0) blobStoreKey = blobStoreKey.substring(0, pos);
	
						previewUrl = null;
						blobFieldValue = blobStoreKey;
					}
				}

				if (requiredWidth != null) {
					if (previewUrl != null && previewUrl.startsWith("/images/view/")) {
						if (previewUrl.indexOf('?') >= 0) previewUrl += '&';
						else previewUrl += '?';
						previewUrl += "width=217";
					}
				}
			}
		}
		else {
			previewUrl = null;
			blobFieldValue = value;
		}

		oldImage.setUrl(previewUrl);
		oldImage.setVisible(previewUrl != null);
		blobField.setValue(blobFieldValue);
	}
}

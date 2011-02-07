package burrito.client.crud.input;

import burrito.client.SiteConstants;
import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.ImageField;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kanal5.play.client.widgets.blobstore.BlobStoreImageField;

@SuppressWarnings("rawtypes")
public class ImageCrudInputField implements CrudInputField {

	private ImageField crudField;
	private VerticalPanel panel;
	private Image oldImage;
	private Integer requiredWidth;
	private Integer requiredHeight;
	private BlobStoreImageField blobField;
	private String url;

	public ImageCrudInputField(ImageField crudField) {
		this.crudField = crudField;

		panel = new VerticalPanel();

		oldImage = new Image();
		panel.add(oldImage);

		requiredWidth = crudField.getWidth();
		requiredHeight = crudField.getHeight();
		if (requiredWidth == 0) requiredWidth = null;
		if (requiredHeight == 0) requiredHeight = null;

		blobField = new BlobStoreImageField(crudField.isRequired(), requiredWidth, requiredHeight);
		panel.add(blobField);

		load(crudField.getValue());
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
			url = "/blobstore/image?key=" + blobStoreKey;
		}
		return url;
	}

	public void load(Object value) {
		url = (String) value;

		String previewUrl = url;
		String blobFieldValue = null;

		if (url != null) {
			if (url.startsWith("/blobstore/image")) {
				int pos = url.indexOf("?key=");
				if (pos < 0) pos = url.indexOf("&key=");
				if (pos >= 0) {
					String blobStoreKey = url.substring(pos + 5);
					pos = blobStoreKey.indexOf('&');
					if (pos >= 0) blobStoreKey = blobStoreKey.substring(0, pos);

					previewUrl = null;
					blobFieldValue = blobStoreKey;
				}
			}

			if (requiredWidth != null && requiredWidth > SiteConstants.tripletImageWidth) {
				if (previewUrl != null && previewUrl.startsWith("/images/view/")) {
					if (previewUrl.indexOf('?') >= 0) previewUrl += '&';
					else previewUrl += '?';
					previewUrl += "width=" + SiteConstants.tripletImageWidth;
				}
			}
		}

		oldImage.setUrl(previewUrl);
		blobField.setValue(blobFieldValue);
	}
}

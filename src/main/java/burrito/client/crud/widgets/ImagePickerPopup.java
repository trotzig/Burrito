package burrito.client.crud.widgets;

import java.util.ArrayList;
import java.util.List;

import burrito.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import burrito.client.widgets.blobstore.BlobStoreUploader;

public class ImagePickerPopup extends DialogBox {

	public static interface SaveHandler {
		void saved(String value);
	}

	private List<SaveHandler> saveHandlers = new ArrayList<SaveHandler>();
	private VerticalPanel wrapper = new VerticalPanel();
	private CrudMessages labels = GWT.create(CrudMessages.class);

	/**
	 * Creates an {@link ImagePickerPopup} that validates uploaded images
	 * against a strictly required width/height. Same as
	 * {@link ImagePickerPopup}(width, height, true)
	 * 
	 * @param width
	 * @param height
	 */

	public ImagePickerPopup(int width, int height) {
		this(width, height, true);
	}

	/**
	 * Creates an {@link ImagePickerPopup} that validates uploaded images
	 * against a required width and height. If strict is true then the uploaded
	 * image will be validated at exactly that size. If strict is false, then
	 * the uploaded image will be validated against a maximum size instead.
	 * 
	 * @param width
	 * @param height
	 * @param strict
	 *            whether or not the uploaded image size must strictly be width
	 *            x height
	 */
	public ImagePickerPopup(int width, int height, boolean strict) {
		super(true);
		setText(labels.uploadImage());
		BlobStoreUploader form = new BlobStoreUploader(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				saved(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to store image. Reason: " + caught.getMessage());
				throw new RuntimeException(caught);
			}
		});
		
		if (strict) {
			wrapper.add(new Label(height == 0 ? labels.requiredImageWidth(width) : labels.requiredImageSize(width, height)));
		} else {
			wrapper.add(new Label(labels.maximumImageSize(width, height)));
		}
		wrapper.add(form);
		setWidget(wrapper);
	}

	public void addSaveHandler(SaveHandler saveHandler) {
		saveHandlers.add(saveHandler);
	}

	private void saved(final String path) {
		for (SaveHandler h : saveHandlers) {
			h.saved(path);
		}
		hide();
	}

}

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

package burrito.client.widgets.blobstore;

import java.util.ArrayList;
import java.util.List;

import burrito.client.widgets.validation.HasValidators;
import burrito.client.widgets.validation.InputFieldValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import burrito.client.widgets.blobstore.BlobStoreUploader;
import burrito.client.widgets.messages.CommonMessages;

public class BlobStoreImageField extends Composite implements HasValidators,
		HasChangeHandlers {

	private boolean required;
	private Integer requiredWidth;
	private Integer requiredHeight;

	private VerticalPanel wrapper = new VerticalPanel();
	private Label validationError = new Label();
	private String blobStoreKey;
	private CommonMessages messages = GWT.create(CommonMessages.class);
	private List<ChangeHandler> changeHandlers = new ArrayList<ChangeHandler>();
	private SimplePanel imageWrapper = new SimplePanel();
	private BlobStoreUploader uploader = null;	
	private Button changeImage = new Button(messages.blobImageChange());
	private Button deleteImage = new Button(messages.blobImageDelete());
	
	
	public BlobStoreImageField(boolean required, Integer requiredWidth, Integer requiredHeight) {
		this.required = required;
		this.requiredWidth = requiredWidth;
		this.requiredHeight = requiredHeight;
		
		validationError.addStyleName("validationError");
		validationError.setVisible(false);
		wrapper.add(validationError);

		String descText;
		if (requiredWidth != null) {
			if (requiredHeight != null) descText = messages.blobImageRequiredSize(requiredWidth, requiredHeight);
			else descText = messages.blobImageRequiredWidth(requiredWidth);
		}
		else {
			if (requiredHeight != null) descText = messages.blobImageRequiredHeight(requiredHeight);
			else descText = messages.blobImageAnySize();
		}
		Label desc = new Label(descText);

		desc.addStyleName("k5-BlobStoreImageField-desc");
		wrapper.add(desc);
		wrapper.add(imageWrapper);
		
		changeImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				uploader.setVisible(true);
				changeImage.setVisible(false);
			}
		});
		wrapper.add(changeImage);
		changeImage.setVisible(false);
		
		deleteImage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				blobStoreKey = null;
				updateImage();
				fireChange();
				updateDeleteButton();
			}
		});
		wrapper.add(deleteImage);
		
		
		updateDeleteButton();
		createNewUploaderWidget();
		initWidget(wrapper);
		addStyleName("k5-BlobStoreImageField");
	}

	private void createNewUploaderWidget() {
		if (uploader != null) {
			uploader.removeFromParent();
		}

		uploader = new BlobStoreUploader(requiredWidth, requiredHeight,
				new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						blobStoreKey = result;
						updateImage();
						fireChange();
						updateDeleteButton();
					}

					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException("Failed to upload image");
					}
				});

		wrapper.add(uploader);
	}

	protected void fireChange() {
		for (ChangeHandler h : changeHandlers) {
			h.onChange(null);
		}
	}

	protected void updateImage() {
		if (blobStoreKey == null) {
			imageWrapper.clear();
			uploader.setVisible(true);
			changeImage.setVisible(false);
		} else {
			imageWrapper.setWidget(new Image("/blobstore/image?s=200&key="
					+ blobStoreKey));
			uploader.setVisible(false);
			changeImage.setVisible(true);
		}
	}
	
	protected void updateDeleteButton() {
		if (blobStoreKey == null) {
			deleteImage.setVisible(false);
		} else {
			deleteImage.setVisible(true);
		}
	}

	@Override
	public void addInputFieldValidator(InputFieldValidator validator) {
		throw new UnsupportedOperationException(
				"Can't add validators to this widget");
	}

	@Override
	public void setValidationError(String verror) {
		if (verror == null) {
			validationError.setText(null);
			validationError.setVisible(false);
		} else {
			validationError.setText(verror);
			validationError.setVisible(true);
		}
	}

	/**
	 * Gets the blob store value for this image
	 * 
	 * @return
	 */
	public String getValue() {
		return blobStoreKey;
	}

	/**
	 * Sets the value of this field. Use blob store keys only.
	 * 
	 * @param blobStoreKey
	 */
	public void setValue(String blobStoreKey) {
		this.blobStoreKey = blobStoreKey;
		createNewUploaderWidget();
		updateImage();
		updateDeleteButton();
	}

	@Override
	public boolean validate() {
		setValidationError(null);
		if (required && getValue() == null) {
			setValidationError(messages.validationRequiredField());
			return false;
		}
		return true;
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		this.changeHandlers.add(handler);
		return null;
	}
}

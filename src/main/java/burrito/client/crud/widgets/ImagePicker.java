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

import java.util.ArrayList;
import java.util.List;



import burrito.client.crud.labels.CrudMessages;
import burrito.client.widgets.validation.HasValidators;
import burrito.client.widgets.validation.InputFieldValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImagePicker extends Composite implements HasChangeHandlers, HasValidators {

	private CrudMessages labels = GWT.create(CrudMessages.class);
	private VerticalPanel wrapper = new VerticalPanel();
	private Image previewThumbnail = new Image();
	private Anchor chooseImage = new Anchor(labels.uploadImage());
	private Anchor removeImage = new Anchor(labels.removeImage());
	private ImagePickerPopup popup;
	private String currentImage;
	private List<ChangeHandler> handlers = new ArrayList<ChangeHandler>();
	private Label validationError = new Label();
	private boolean required;
	
	public ImagePicker(boolean required, String value, int width, int height) {
		setValue(value);
		this.required = required;
		validationError.addStyleName("validationError");
		validationError.setVisible(false);
		popup = new ImagePickerPopup(width, height);
		previewThumbnail.setSize("120px", "67px");
		previewThumbnail.addStyleName("k5-ImagePicker-thumb");
		previewThumbnail.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (currentImage != null) {
					Window.open(currentImage, "_blank", null);
				}
			}
		});
		popup.addSaveHandler(new ImagePickerPopup.SaveHandler() {

			public void saved(String value) {
				setValue("/images/view/" + value);
			}
		});
		chooseImage.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				popup.center();
				popup.show();
			}
		});
		chooseImage.addStyleName("k5-ImagePicker-choose");

		removeImage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setValue(null);
			}
		});

		Label label = new Label(height == 0 ? labels.requiredImageWidth(width) : labels.requiredImageSize(width, height));
		label.addStyleName("k5-ImagePicker-description");
		wrapper.add(label);
		wrapper.add(validationError);
		wrapper.add(previewThumbnail);
		wrapper.add(chooseImage);
		wrapper.add(removeImage);
		initWidget(wrapper);
		addStyleName("k5-ImagePicker");
	}

	public String getValue() {
		return currentImage;
	}

	public void setValue(String value) {
		currentImage = value;
		if (value == null) {
			previewThumbnail.setVisible(false);
		} else {
			previewThumbnail.setUrl(value.replace("/images/view/",
					"/images/view/thumb_"));
			previewThumbnail.setVisible(true);
		}
		fireChange();
	}

	private void fireChange() {
		for (ChangeHandler handler : handlers) {
			handler.onChange(null);
		}
	}

	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		handlers.add(handler);
		return null;
	}

	public void addInputFieldValidator(InputFieldValidator validator) {
		throw new UnsupportedOperationException();
	}

	public void setValidationError(String errorText) {
		validationError.setVisible(false);
		if (errorText != null) {
			validationError.setText(errorText);
			validationError.setVisible(true);
		}
	}

	public boolean validate() {
		setValidationError(null);
		if (getValue() == null && required) {
			setValidationError(labels.mustAddImage());
			return false;
		}
		return true;
	}
	

	@Override
	public void highlight() {
		
	}


}

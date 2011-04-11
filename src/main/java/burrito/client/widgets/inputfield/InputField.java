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

package burrito.client.widgets.inputfield;

import java.util.ArrayList;
import java.util.List;

import burrito.client.widgets.validation.HasValidators;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.RequiredValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public abstract class InputField<T> extends Composite implements
		HasChangeHandlers, HasValidators, HasKeyDownHandlers {

	private TextBoxBase field;
	private Label validationError;
	private List<InputFieldValidator> validators = new ArrayList<InputFieldValidator>();
	private Grid wrapper = new Grid(1, 4);
	private Label requiredStar;
	private boolean required;

	/**
	 * Constructs an InputField.
	 * 
	 * @param required
	 *            Use <code>true</true> if this field should be required
	 */
	public InputField(boolean required) {
		this.required = required;
		field = createField();
		validationError = new Label();
		validationError.addStyleName("validationError");
		setValidationError(null);
		wrapper.setWidget(0, 0, field);
		requiredStar = new Label("*");
		if (required) {
			wrapper.setWidget(0, 1, requiredStar);
			addInputFieldValidator(new RequiredValidator());
		}
		initWidget(wrapper);
		setStyleName("k5-InputField");
	}

	/**
	 * Adds a validator to this input field
	 * 
	 * @param validator
	 */
	@Override
	public void addInputFieldValidator(InputFieldValidator validator) {
		validators.add(validator);
	}

	/**
	 * Positions a widget in between the required star and the validation
	 * message.
	 * 
	 * @param squeezedIn
	 */
	public void setSqueezedInWidget(Widget squeezedIn) {
		wrapper.setWidget(0, 2, squeezedIn);
	}

	/**
	 * Validates the content of the input field with all added validators.
	 * 
	 * @param displayErrorMessage
	 *            if true, this method will also automatically display a
	 *            validation error message if one of the validators fail.
	 * @return
	 */
	public boolean validate(boolean displayErrorMessage) {
		setValidationError(null);
		if (!field.isEnabled()) {
			return true;
		}
		String text = field.getText();
		if (!required && (text == null || text.isEmpty())) {
			return true;
		}
		try {
			for (InputFieldValidator validator : validators) {
				validator.validate(text);
			}
			return true;
		} catch (ValidationException e) {
			if (displayErrorMessage) {
				setValidationError(e.getValidationMessage());
				setFocus(true);
				selectAll();
			}
			return false;
		}
	}

	/**
	 * Validates the content of the input field with all added validators. This
	 * method will also automatically display a validation error message if one
	 * of the validators fail.
	 */
	public boolean validate() {
		return validate(true);
	}

	/**
	 * Sets a validation error on this input field
	 * 
	 * @param validationMessage
	 */
	@Override
	public void setValidationError(String validationMessage) {
		if (validationMessage == null) {
			validationError.setText(null);
			wrapper.clearCell(0, 3);
		} else {
			validationError.setText(validationMessage);
			wrapper.setWidget(0, 3, validationError);
		}
	}

	/**
	 * Creates a text box to display the value in
	 * 
	 * @return
	 */
	protected TextBoxBase createField() {
		return new TextBox();
	}

	/**
	 * Adds a click handler
	 * 
	 * @param handler
	 */
	public void addClickHandler(ClickHandler handler) {
		field.addClickHandler(handler);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return field.addChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return field.addKeyDownHandler(handler);
	}

	/**
	 * Gets the value contained in this inputfield. Returns null if the field
	 * does not validate
	 * 
	 * @return
	 */
	public T getValue() {
		String v = field.getText();
		if (v == null || v.isEmpty()) {
			return null;
		}
		return parseValue(field.getText());
	}

	/**
	 * Parses a String and returns an object of T type.
	 * 
	 * @param text
	 * @return
	 */
	protected abstract T parseValue(String text);

	/**
	 * Sets the value
	 * 
	 * @param value
	 */
	public void setValue(T value) {
		if (value == null) {
			field.setText(null);
		} else {
			field.setText(value.toString());
		}

	}

	/**
	 * Selects all text contained in the text box
	 */
	public void selectAll() {
		field.selectAll();
	}

	/**
	 * Sets focus to the field
	 * 
	 * @param focus
	 */
	public void setFocus(boolean focus) {
		field.setFocus(focus);
	}

	/**
	 * Enables or disables the text input field
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		field.setEnabled(enabled);
		requiredStar.setVisible(enabled);
	}

}

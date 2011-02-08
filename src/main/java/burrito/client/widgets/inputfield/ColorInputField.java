package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.StringInputField;
import burrito.client.widgets.inputfield.ValidationMessages;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;

public class ColorInputField extends StringInputField {

	private ValidationMessages messages = GWT.create(ValidationMessages.class);
	
	public ColorInputField(boolean required) {
		super(required);
		addInputFieldValidator(new InputFieldValidator() {
			
			@Override
			public void validate(String value) throws ValidationException {
				if (!value.matches("^[a-zA-Z0-9]{6}")) {
					throw new ValidationException(messages.colorNotValid());
				}
			}
		});
	}

}

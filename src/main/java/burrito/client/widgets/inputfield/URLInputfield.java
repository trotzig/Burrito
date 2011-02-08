package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.StringInputField;
import burrito.client.widgets.inputfield.ValidationMessages;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;

public class URLInputfield extends StringInputField {

	private ValidationMessages messages = GWT.create(ValidationMessages.class);
	
	public URLInputfield(boolean required) {
		super(required);
		addInputFieldValidator(new InputFieldValidator() {
			@Override
			public void validate(String value) throws ValidationException {
				if (!value.matches("^https?:\\/\\/.*")) {
					throw new ValidationException(messages.validateURL());
				}
			}
		});
		addStyleName("k5-URLInputfield");
	}

}

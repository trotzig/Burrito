package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.StringInputField;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

public class EmailInputField extends StringInputField {

	private CommonMessages messages = GWT.create(CommonMessages.class);
	
	public EmailInputField(boolean required) {
		super(required);
		addInputFieldValidator(new InputFieldValidator() {
		
			@Override
			public void validate(String value) throws ValidationException {
				if (value.matches(".+@.+")) {
					return;
				}
				throw new ValidationException(messages.validationInvalidEmail());
			}
		});
	}

}

package burrito.client.widgets.validation;

import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

public class MaxLengthInputFieldValidator implements InputFieldValidator {

	private CommonMessages messages = GWT.create(CommonMessages.class);

	private int maxChars;
	
	public MaxLengthInputFieldValidator(int maxChars) {
		this.maxChars = maxChars;
	}
	
	@Override
	public void validate(String value) throws ValidationException {
		if(value != null && value.length() > maxChars) {
			throw new ValidationException(messages.validationTooLong(maxChars));
		}
	}

}

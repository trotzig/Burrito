package burrito.client.widgets.validation;

import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

/**
 * Required field validator
 * 
 * @author henper
 * 
 */
public class RequiredValidator implements InputFieldValidator {

	private CommonMessages messages = GWT.create(CommonMessages.class);
	
	@Override
	public void validate(String value) throws ValidationException {
		if (value == null || value.isEmpty()) {
			throw new ValidationException(messages.validationRequiredField());
		}
	}

}

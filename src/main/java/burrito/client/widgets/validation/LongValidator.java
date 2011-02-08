package burrito.client.widgets.validation;

import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

/**
 * Input field validator for Long
 * 
 * @author Joakim S�derstr�m, Elvagruppen AB
 *
 */
public class LongValidator implements InputFieldValidator {

	private CommonMessages messages = GWT.create(CommonMessages.class);
	
	@Override
	public void validate(String value) throws ValidationException {
		try {
			Long.parseLong(value);
		} catch(NumberFormatException e) {
			throw new ValidationException(messages.validationNotNumber());
		}
	}

}

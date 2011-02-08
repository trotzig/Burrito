package burrito.client.widgets.validation;

import burrito.client.widgets.validation.ValidationException;

public interface InputFieldValidator {

	/**
	 * Validates a value. 
	 * @param value
	 * @return
	 */
	void validate(String value) throws ValidationException;
	
}

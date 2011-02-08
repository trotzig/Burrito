package burrito.client.widgets.validation;

import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

public interface HasValidators {

	/**
	 * Adds a validator
	 * 
	 * @param validator
	 */
	void addInputFieldValidator(InputFieldValidator validator);

	/**
	 * Validates against all added validators
	 * 
	 * @throws ValidationException
	 */
	boolean validate();

	/**
	 * Manually sets a validation error
	 * 
	 * @param validationError
	 */
	void setValidationError(String validationError);

}

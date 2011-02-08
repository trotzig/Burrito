package burrito.client.widgets.validation;

import burrito.client.widgets.validation.ValidationException;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	private String validationMessage;

	/**
	 * Constructs a new {@link ValidationException} with a specified
	 * validationMessage. This message must be safe to display to a user. 
	 * <br/>
	 * Good example: "Please enter a number between 1 to 99"
	 * <br/>
	 * Bad example (avoid): "Not a valid integer"
	 * 
	 * @param validationMessage
	 */
	public ValidationException(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	/**
	 * Gets a message that describes the validation error
	 * 
	 * @return
	 */
	public String getValidationMessage() {
		return validationMessage;
	}

}

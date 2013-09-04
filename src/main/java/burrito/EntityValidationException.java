package burrito;

/**
 * Throw this when you are about to store a model to signal that something does
 * not validate.
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class EntityValidationException extends RuntimeException {

	private String fieldName;

	public EntityValidationException(String message) {
		super(message);
	}

	public EntityValidationException(String message, String fieldName) {
		super(message);
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}

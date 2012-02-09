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

	public EntityValidationException(String message) {
		super(message);
	}

}
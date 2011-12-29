package burrito.util;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;

import siena.SienaException;
import burrito.ValidationException;

/**
 * Helper for model classes/entities
 * 
 * @author henper
 * 
 */
public class ValidationUtil {

	/**
	 * Checks a siena exception for a possible {@link ValidationException}
	 * cause. If not, the siena exception is rethrown. Else, the
	 * {@link ValidationException} is returned
	 * 
	 * @param e
	 *            the siena exception
	 * @return
	 * @throws SienaException
	 *             in case the cause of the siena exception was not a
	 *             {@link ValidationException}
	 */
	public static ValidationException getValidationErrorOrRethrow(
			SienaException e) throws SienaException {
		@SuppressWarnings("unchecked")
		List<Throwable> throwableList = ExceptionUtils.getThrowableList(e);
		for (Throwable throwable : throwableList) {
			if (throwable instanceof ValidationException) {
				ValidationException ve = (ValidationException) throwable;
				return ve;
			}
		}
		throw e;
	}

}

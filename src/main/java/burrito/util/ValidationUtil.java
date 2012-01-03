package burrito.util;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;

import siena.Model;
import siena.SienaException;
import burrito.EntityValidationException;
import burrito.annotations.Required;

/**
 * Helper for model classes/entities
 * 
 * @author henper
 * 
 */
public class ValidationUtil {

	/**
	 * Checks a siena exception for a possible {@link EntityValidationException}
	 * cause. If not, the siena exception is rethrown. Else, the
	 * {@link EntityValidationException} is returned
	 * 
	 * @param e
	 *            the siena exception
	 * @return
	 * @throws SienaException
	 *             in case the cause of the siena exception was not a
	 *             {@link EntityValidationException}
	 */
	public static EntityValidationException getValidationErrorOrRethrow(
			SienaException e) throws SienaException {
		@SuppressWarnings("unchecked")
		List<Throwable> throwableList = ExceptionUtils.getThrowableList(e);
		for (Throwable throwable : throwableList) {
			if (throwable instanceof EntityValidationException) {
				EntityValidationException ve = (EntityValidationException) throwable;
				return ve;
			}
		}
		throw e;
	}

	/**
	 * Provides simple validation for an entity.
	 * 
	 * Throws an {@link EntityValidationException} if any
	 * required field is null.
	 * 
	 * @param model
	 *            The entity to validate
	 * @return
	 * @throws {@link EntityValidationException}
	 */
	public static void assertNoMissingRequiredFields(Model entity) {
		Class<?> clazz = entity.getClass();

		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Required.class)) {
				try {
					field.setAccessible(true);

					if (field.get(entity) == null) {
						throw new EntityValidationException("Missing value for required field " + field.getName());
					}
				}
				catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				}
				catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}

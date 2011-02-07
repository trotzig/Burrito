package burrito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RegexpValidation {

	/**
	 * The regexp pattern to validate against
	 * 
	 * @return
	 */
	String pattern();

	/**
	 * The description of the pattern
	 * @return
	 */
	String description();

}

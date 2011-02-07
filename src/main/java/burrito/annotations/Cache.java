package burrito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to signal that an entity can be cloned in the GWT CRUD admin
 * 
 * @author henper
 * 
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
	/**
	 * Enables or disables the cache.
	 * 
	 * @return
	 */
	boolean enabled() default true;

	/**
	 * The expiration time in seconds. Default value is 300 s, 5 min.
	 * 
	 * @return
	 */
	int expirationInSeconds() default 300;
}

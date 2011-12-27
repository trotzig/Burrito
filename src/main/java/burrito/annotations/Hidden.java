package burrito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation makes a field hidden from CRUD-admin. Consider using @RedundantForPerformance
 * if you need to be more explicit about why this field is hidden.
 * 
 * @author henper
 * 
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Hidden {
	// no need for attributes
}

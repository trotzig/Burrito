package burrito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that can be used to hide a field from admin. Same as @Hidden, but
 * with a more semantic meaning. This annotation should be used in cases where
 * you need to store something that is redundant, but needed for performance
 * reasons.
 * 
 * @author henper
 * 
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RedundantForPerformance {
	// no need for attributes
}

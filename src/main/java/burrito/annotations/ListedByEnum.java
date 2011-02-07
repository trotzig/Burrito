package burrito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used on String fields that will result in a selection list in the
 * admin interface. The values in the selection list are taken from the
 * specified enum.
 * 
 * @author henper
 * 
 */
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ListedByEnum {

	Class<? extends Enum<?>> type();
}

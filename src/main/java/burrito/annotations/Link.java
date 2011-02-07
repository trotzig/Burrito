package burrito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import burrito.links.Linkable;



/**
 * Annotation used on entity fields that represent one or more links to other,
 * {@link Linkable}, entities.
 * 
 * @author henper
 * 
 */
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Link {
	//empty
}

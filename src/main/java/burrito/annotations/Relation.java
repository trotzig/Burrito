package burrito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import siena.Model;

/**
 * Annotation used in relations between two different datastore entities. Usage:
 * <code>
 * 
 * @Relation(Program.class) public Long relatedProgram;
 * 
 * @Relation(Program.class) public List<Long> relatedPrograms;
 * 
 *                          </code>
 * 
 * @author henper
 * 
 */
 @Target({ElementType.FIELD})
 @Retention(RetentionPolicy.RUNTIME)
public @interface Relation {

	/**
	 * The entity class that the annotated field relates to
	 * 
	 * @return
	 */
	Class<? extends Model> value();
}

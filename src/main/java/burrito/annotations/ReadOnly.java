package burrito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Används för att annotera fält i model-klasser,
 * för att det inte ska gå att redigera i admin
 * 
 * @author mikcla
 */

@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadOnly {

}

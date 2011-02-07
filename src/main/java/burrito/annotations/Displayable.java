package burrito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Används för att annotera fält och/eller metoder i model-klasser
 * 
 * Gör så att värdet visas i en kolumn i adminlistan
 * 
 * @author mikcla
 */
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Displayable {
}

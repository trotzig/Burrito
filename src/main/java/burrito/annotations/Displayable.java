/**
 * Copyright 2011 Henric Persson (henric.persson@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package burrito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate fields and/or methods in model classes.
 * 
 * Makes the value display in a column in the CRUD admin view.
 * 
 * Methods that are annotated can return object of most types.
 * 
 * @author mikcla
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Displayable {
	/**
	 * Set this field to true to force the crud field to be displayed at the
	 * last column of the crud table
	 * 
	 * @return
	 */
	boolean last() default false;
	
	
	/**
	 * Use this on Displayable annotations on Boolean fields/methods to display a
	 * special icon when the value is <code>true</code>.
	 * 
	 * @return
	 */
	String iconUrlOnTrue() default "";
	
	/**
	 * Use this on Displayable annotations on String fields/methods to treat the
	 * value as an icon URL. Will display as an icon in the CRUD admin view.
	 */
	boolean useAsIconUrl() default false;
	
	
}

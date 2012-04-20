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

	public enum RenderMode {
		DROPDOWN("dropdown"),
		SEARCH("search");
		
		private String value2;

		RenderMode(String value) {
			this.value2 = value;
		}
		
		public String getValue() {
			return value2;
		}
	}
	 
	RenderMode renderMode() default RenderMode.DROPDOWN;
	
	/**
	 * Required if renderMode = SEARCH
	 * @return
	 */
	String searchSortField() default "";
}

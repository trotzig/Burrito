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

package burrito.client.crud.generic.fields;

import java.util.List;

import burrito.client.crud.generic.CrudField;

/**
 * A {@link CrudField} representating a selection of enums
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class EnumListField extends StringListField {

	private String enumClassName;

	public EnumListField() {
		super();
		// default constructor
	}

	public EnumListField(List<String> strings, String enumClassName) {
		super(strings);
		this.setEnumClassName(enumClassName);
	}

	public void setEnumClassName(String enumClassName) {
		this.enumClassName = enumClassName;
	}

	public String getEnumClassName() {
		return enumClassName;
	}

}

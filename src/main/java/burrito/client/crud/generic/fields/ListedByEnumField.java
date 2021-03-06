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

import java.io.Serializable;

import burrito.client.crud.generic.CrudField;




@SuppressWarnings("serial")
public class ListedByEnumField extends CrudField implements Serializable {

	private String value;
	private String typeClassName;

	public ListedByEnumField(String value, String typeClassName) {
		this.value = value;
		this.typeClassName = typeClassName;
	}

	public ListedByEnumField() {
		// Default constructor
	}

	@Override
	public Class<?> getType() {
		return Enum.class;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = (String) value;
	}
	
	public void setTypeClassName(String typeClassName) {
		this.typeClassName = typeClassName;
	}
	
	public String getTypeClassName() {
		return typeClassName;
	}

}

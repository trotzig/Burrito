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

public class EnumIndexedListField extends CrudField {

	private static final long serialVersionUID = -1622137165818184232L;

	private List<Object> value;
	private String enumClassName;
	private String valueListClassName;
	
	public EnumIndexedListField() {
		// default constructor
	}
	
	/**
	 * 
	 * @param value
	 * @param enumClassName
	 * @param valueListClassName List<valueListClassName> value
	 */
	public EnumIndexedListField(List<Object> value, String enumClassName, String valueListClassName) {
		super();
		
		this.valueListClassName = valueListClassName;
		this.setValue(value);
		this.setEnumClassName(enumClassName);
	}

	public void setEnumClassName(String enumClassName) {
		this.enumClassName = enumClassName;
	}

	public String getEnumClassName() {
		return enumClassName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.value = (List<Object>) value;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Class<?> getType() {
		return List.class;
	}
	
	public String getListClassName() {
		return valueListClassName;
	}
}

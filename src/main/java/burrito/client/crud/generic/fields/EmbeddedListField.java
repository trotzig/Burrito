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

import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.generic.CrudField;




public class EmbeddedListField extends CrudField {

	private static final long serialVersionUID = 1L;

	private List<CrudEntityDescription> embedded;
	private String embeddedClassName;
	
	
	public EmbeddedListField(List<CrudEntityDescription> embedded, String embeddedClassName) {
		this.embedded = embedded;
		this.embeddedClassName = embeddedClassName;
	}
	
	public EmbeddedListField() {
		//default constructor
	}
	
	public String getEmbeddedClassName() {
		return embeddedClassName;
	}
	
	@Override
	public Class<?> getType() {
		return List.class;
	}

	@Override
	public Object getValue() {
		return embedded;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.embedded = (List<CrudEntityDescription>) value;
	}

}

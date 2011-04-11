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
 * A {@link CrudField} representating an many-to-many relation
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class ManyToManyRelationField extends CrudField {

	private List<Long> list;
	private String relatedEntityName;

	public ManyToManyRelationField() {
		// default constructor
	}

	public ManyToManyRelationField(List<Long> list, String relatedEntityName) {
		this.list = list;
		this.relatedEntityName = relatedEntityName;
	}

	@Override
	public Class<?> getType() {
		return List.class;
	}

	@Override
	public Object getValue() {
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.list = (List<Long>) value;
	}

	/**
	 * The class name of the related entity
	 * 
	 * @return
	 */
	public String getRelatedEntityName() {
		return relatedEntityName;
	}

	public void setRelatedEntityClass(String relatedEntityName) {
		this.relatedEntityName = relatedEntityName;
	}

}

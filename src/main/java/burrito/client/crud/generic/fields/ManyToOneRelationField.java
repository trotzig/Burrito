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

import burrito.client.crud.generic.CrudField;

@SuppressWarnings("serial")
public class ManyToOneRelationField extends CrudField {

	private Long id;
	private String relatedEntityName;
	private String modeValue;
	private String searchSortField;

	public ManyToOneRelationField(Long id, String relatedEntityName, String modeValue, String searchSortField) {
		this.id = id;
		this.relatedEntityName = relatedEntityName;
		this.modeValue = modeValue;
		this.searchSortField = searchSortField;
	}

	public ManyToOneRelationField() {
		// default constructor, needed by GWT
	}

	@Override
	public Class<?> getType() {
		return Long.class;
	}

	@Override
	public Object getValue() {
		return id;
	}

	@Override
	public void setValue(Object value) {
		this.id = (Long) value;
	}
	
	public boolean isDropDown() {
		return "dropdown".equalsIgnoreCase(modeValue);
	}
	
	public String getSearchSortField() {
		return searchSortField;
	}
	
	/**
	 *  Get namespace + class name
	 * @return ex model.TestEntity
	 */
	public String getRelatedEntityName() {
		return relatedEntityName;
	}

	public void setRelatedEntityName(String relatedEntityName) {
		this.relatedEntityName = relatedEntityName;
	}
}

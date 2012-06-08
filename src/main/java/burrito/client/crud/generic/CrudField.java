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

package burrito.client.crud.generic;

import java.io.Serializable;
import java.util.Date;

/**
 * Abstract description of an entity field. Gives hints to the UI code as to how
 * the field should be edited (which widget should be used, what validation
 * should occur, etc). This class should be using generics, e.g. CrudField<T>,
 * where T is the class type that this field represents. But the details of the
 * GWT RPC functionality prevents this approach.
 * 
 * @author henper
 * 
 */
public abstract class CrudField implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private boolean required = false;
	private boolean readOnly = false;
	private boolean searchable = false;
	private boolean defaultSort = false;
	private boolean sortAscending = true;
	private boolean useAsIconUrl = false;
	private String iconUrlOnTrue;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the value held by this field
	 * 
	 * @return
	 */
	public abstract Object getValue();

	/**
	 * Gets the type of the field. Supported types can be found in external
	 * documentation, but you should safely be able to use {@link String},
	 * {@link Date} and {@link Integer}.
	 * 
	 * @return
	 */
	public abstract Class<?> getType();

	/**
	 * Sets the value of this field
	 * 
	 * @param value
	 */
	public abstract void setValue(Object value);

	/**
	 * Sets whether this field is a required field or not
	 * 
	 * @param required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Returns true if this field is required. Input widgets should acknowledge
	 * this property and use proper validation.
	 * 
	 * @return
	 */
	public boolean isRequired() {
		return required;
	}
	
	
	/**
	 * Sets whether this field is a readonly field or not
	 * 
	 * @param readOnly
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/**
	 * Returns true if the field is readonly
	 * 
	 * @return
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Sets whether this field should be searchable or not
	 * 
	 * @param searchable
	 */
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	/**
	 * Returns true if the field is searchable
	 * 
	 * @return
	 */
	public boolean isSearchable() {
		return searchable;
	}

	/**
	 * Sets whether this field is the default sort field for the entity
	 * 
	 * @param defaultSort
	 */
	public void setDefaultSort(boolean defaultSort) {
		this.defaultSort = defaultSort;
	}

	/**
	 * Returns true if this field should be the default table sort column
	 * 
	 * @return
	 */
	public boolean isDefaultSort() {
		return defaultSort;
	}
	
	public boolean isSortAscending() {
		return sortAscending;
	}
	
	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}

	/**
	 * Returns true if this field can be sorted on
	 * 
	 * @return
	 */
	public boolean isSortable() {
		return true;
	}
	

	public boolean isUseAsIconUrl() {
		return useAsIconUrl;
	}

	public void setUseAsIconUrl(boolean useAsIconUrl) {
		this.useAsIconUrl = useAsIconUrl;
	}

	public String getIconUrlOnTrue() {
		return iconUrlOnTrue;
	}

	public void setIconUrlOnTrue(String iconUrlOnTrue) {
		this.iconUrlOnTrue = iconUrlOnTrue;
	}
	
	
}

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
import java.util.ArrayList;
import java.util.List;

/**
 * This class describes the capabilities of an entity and holds its values.
 * 
 * @author henper
 * 
 */
public class CrudEntityDescription implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<CrudField> fields;
	private String entityName;
	private Long id;
	private String displayString;
	private boolean cloneable = false;

	public CrudEntityDescription() {
		fields = new ArrayList<CrudField>();
	}

	/**
	 * Adds a field
	 * 
	 * @param field
	 */
	public void add(CrudField field) {
		fields.add(field);
	}

	/**
	 * Gets all fields for this entity
	 * 
	 * @return
	 */
	public List<CrudField> getFields() {
		return fields;
	}

	/**
	 * Sets the fields for this entity
	 * 
	 * @param fields
	 */
	public void setFields(ArrayList<CrudField> fields) {
		this.fields = fields;
	}

	/**
	 * Gets the entity name. This is the name of the class that this instance
	 * belongs to.
	 * 
	 * @return
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Sets the entity name
	 * 
	 * @param entityName
	 *            The class name of the entity, e.g. "models.Program"
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Gets the database id of the entity
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the database id of the entity.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns true if this description describes a new entity. Returns false if
	 * this entity is already in the db.
	 * 
	 * @return
	 */
	public boolean isNew() {
		if (id == null) {
			return true;
		}
		if (id.longValue() == -1) {
			return true;
		}
		return false;
	}

	/**
	 * Sets the display string. This is normally the value returned from calling
	 * toString() on the entity.
	 * 
	 * @param displayString
	 */
	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}

	/**
	 * Gets the display string. This is normally the value returned from calling
	 * toString() on the entity.
	 * 
	 * @return
	 */
	public String getDisplayString() {
		return displayString;
	}

	/**
	 * Gets the value of a certain field
	 * 
	 * @param name
	 * @return
	 */
	public Object getValue(String fieldName) {
		for (CrudField field : fields) {
			if (fieldName.equals(field.getName())) {
				return field.getValue();
			}
		}
		return null;
	}

	public void setCloneable(boolean cloneable) {
		this.cloneable = cloneable;
	}
	
	public boolean isCloneable() {
		return cloneable;
	}
}

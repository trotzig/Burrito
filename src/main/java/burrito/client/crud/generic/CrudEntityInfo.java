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

/**
 * Simple description of a crud enabled entity
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class CrudEntityInfo implements Serializable {

	private String entityName;

	public CrudEntityInfo() {
		// default constructor
	}

	public CrudEntityInfo(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Gets the entity name. This is usually the classname of a database entity,
	 * e.g. "models.Program"
	 * 
	 * @return
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Sets the entity name. This is usually the classname of a database entity,
	 * e.g. "models.Program"
	 * 
	 * @param entityName
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

}

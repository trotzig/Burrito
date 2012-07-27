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

package burrito.client.crud;

import java.util.List;
import java.util.Set;

import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.generic.CrudEntityInfo;
import burrito.client.crud.generic.CrudEntityList;
import burrito.client.widgets.panels.table.PageMetaData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("crud")
public interface CrudService extends RemoteService {

	/**
	 * Describes an entity. The description will contain meta data about the
	 * entity fields and also hold its values.
	 * 
	 * @param entityName
	 *            The class name of the entity to edit, e.g. "models.Program"
	 * @param id
	 *            the id of the entity to describe. If -1, then a new instance
	 *            of the entity is described
	 * @param copyFromId
	 *            <code>null</code> or a database id of an entity to copy values
	 *            from
	 * @return
	 */
	CrudEntityDescription describe(String entityName, Long id, Long copyFromId);

	/**
	 * Saves an entity to the database. If a new instance is created the
	 * instance will be inserted into the db. If an existing one is being
	 * edited, this instance will be updated in the db.
	 * 
	 * @param updateCrudEntityDescription
	 * @return the id of the created or updated entity
	 * @throws CrudGenericException 
	 */
	Long save(CrudEntityDescription updateCrudEntityDescription, Long copyFromId) throws FieldValueNotUniqueException, CrudGenericException;

	/**
	 * Lists all database rows from a given entity.
	 * 
	 * @param entityName
	 *            The class name of the entity to edit, e.g. "models.Program"
	 * @param p
	 *            the current page to show.
	 * @return
	 */
	CrudEntityList listEntities(String filter, String entityName,
			PageMetaData<String> p);


	/**
	 * Deletes a set of entities from the db.
	 * 
	 * @param selected
	 */
	void deleteEntities(List<CrudEntityDescription> selected);

	/**
	 * Gets all crud enabled entities
	 * 
	 * @return
	 */
	List<CrudEntityInfo> getAllEntities();

	/**
	 * Checks if class is a crud enabled entity
	 * 
	 * @return
	 */
	Boolean isCrudEnabled(String className);

	/**
	 * Used to fetch headers for a specific entity. Can be used in a table
	 * listing.
	 * 
	 * @param entityName
	 *            The class name of the entity to fetch headers for, e.g.
	 *            "models.Program"
	 * @return
	 */
	CrudEntityDescription getEntityHeaders(String entityName);

	/**
	 * Gets all rows from the db of a specific entity.
	 * 
	 * @param entityName
	 *            The class name of the entity to list, e.g. "models.Program"
	 * @return
	 */
	List<CrudNameIdPair> getListValues(String entityName);

	/**
	 * List of values from an enum type.
	 * 
	 * @param type
	 * @return
	 */
	List<String> getEnumListValues(String classNameType);

	/**
	 * Gets entity types that can be linked
	 * 
	 * @return
	 */
	List<String> getLinkableTypes();

	/**
	 * Describes an embedded object in an entity
	 * 
	 * @param embeddedClassName
	 * @return
	 */
	CrudEntityDescription describeEmbeddedObject(String embeddedClassName);
	
	/**
	 * Gets display names for a number of entities
	 * 
	 * @param references
	 * @return
	 */
	List<CrudEntityReference> getDisplayNames(Set<CrudEntityReference> references);

	/**
	 * Gets the data required for an entity preview
	 * 
	 * @param references
	 * @return
	 */
	CrudPreviewPayload getPreviewPayload(CrudEntityDescription desc);
	
	/**
	 * Counts rows for an entity
	 * 
	 * @param entityClassName
	 * @return
	 */
	Integer count(String entityClassName);

	/**
	 * Reindex an entity
	 * 
	 * @param cursor
	 * @return
	 */
	void reindex(String entityClassName, PageMetaData<String> page);

	/**
	 * Clears all index entries for a certain entity
	 * 
	 * @param entityName
	 */
	void clearIndexForEntity(String entityName);
	
}

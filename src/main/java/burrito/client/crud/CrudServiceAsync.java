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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The asynchronous version of {@link CrudService}
 * 
 * @author henper
 * 
 */
public interface CrudServiceAsync {

	void describe(String entityName, Long id,
			Long copyFromId, AsyncCallback<CrudEntityDescription> callback);

	void save(CrudEntityDescription updateCrudEntityDescription, Long copyFromId,
			AsyncCallback<Long> asyncCallback);

	void listEntities(String filter, String entityName, PageMetaData<String> p,
			AsyncCallback<CrudEntityList> asyncCallback);
	
	void deleteEntities(List<CrudEntityDescription> selected,
			AsyncCallback<Void> callback);

	void getAllEntities(AsyncCallback<List<CrudEntityInfo>> asyncCallback);

	void isCrudEnabled(String className, AsyncCallback<Boolean> asyncCallback);

	void getEntityHeaders(String entityName,
			AsyncCallback<CrudEntityDescription> asyncCallback);

	void getListValues(String entityName,
			AsyncCallback<List<CrudNameIdPair>> asyncCallback);


	void getEnumListValues(String classNameType,
			AsyncCallback<List<String>> asyncCallback);

	void getLinkableTypes(AsyncCallback<List<String>> asyncCallback);

	void describeEmbeddedObject(String embeddedClassName,
			AsyncCallback<CrudEntityDescription> asyncCallback);

	void getDisplayNames(Set<CrudEntityReference> references,
			AsyncCallback<List<CrudEntityReference>> asyncCallback);

	void getPreviewPayload(CrudEntityDescription desc, AsyncCallback<CrudPreviewPayload> asyncCallback);

	void count(String entityName, AsyncCallback<Integer> asyncCallback);

	void reindex(String entityName, PageMetaData<String> page, AsyncCallback<Void> asyncCallback);
	
	void clearIndexForEntity(String entityName, AsyncCallback<Void> callback);

	void reindexPartial(String entityName, AsyncCallback<Void> asyncCallback);
}

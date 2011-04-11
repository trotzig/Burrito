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

package burrito.client.sitelet;

import java.util.List;

import burrito.client.crud.generic.CrudEntityInfo;
import burrito.client.dto.SiteletDescription;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SiteletServiceAsync {

	void getSitelets(String containerId,
			AsyncCallback<List<SiteletDescription>> asyncCallback);

	void getSitelet(Long siteletId, AsyncCallback<SiteletDescription> asyncCallback);

	void addSitelet(String containerName, String entityName, Long savedId,
			boolean addOnTop, AsyncCallback<Void> asyncCallback);

	void saveSiteletOrder(String containerName, List<Long> longOrder,
			AsyncCallback<Void> asyncCallback);

	void deleteSitelets(String containerName, List<Long> ids, AsyncCallback<Void> callback);

	void getSiteletTypes(AsyncCallback<List<CrudEntityInfo>> asyncCallback);
	
	void clearCache(Long siteletId, AsyncCallback<Void> callback);

}

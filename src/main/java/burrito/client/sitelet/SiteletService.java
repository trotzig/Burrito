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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sitelets")
public interface SiteletService extends RemoteService {

	List<SiteletDescription> getSitelets(String containerId);

	SiteletDescription getSitelet(Long id);

	void addSitelet(String containerName, String entityName, Long savedId, boolean addOnTop);

	void saveSiteletOrder(String containerName, List<Long> longOrder);

	void deleteSitelets(String containerName, List<Long> ids);

	List<CrudEntityInfo> getSiteletTypes();

	/**
	 * Clears the sitelet cache
	 * 
	 */
	void clearCache(Long id);

}

package burrito.client.sitelet;

import java.util.List;



import burrito.client.crud.CrudNameIdPair;
import burrito.client.crud.generic.CrudEntityInfo;
import burrito.client.dto.SiteletDescription;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sitelets")
public interface SiteletService extends RemoteService {

	List<CrudNameIdPair> getSitelets(String containerId);

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

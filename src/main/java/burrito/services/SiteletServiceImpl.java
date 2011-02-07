package burrito.services;

import java.util.ArrayList;
import java.util.List;

import siena.Model;
import burrito.Configurator;
import burrito.client.crud.CrudNameIdPair;
import burrito.client.crud.generic.CrudEntityInfo;
import burrito.client.dto.SiteletDescription;
import burrito.client.sitelet.SiteletService;
import burrito.sitelet.Sitelet;
import burrito.util.Cache;
import burrito.util.Logger;
import burrito.util.SiteletHelper;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
public class SiteletServiceImpl extends RemoteServiceServlet implements SiteletService {

	private static final long serialVersionUID = 1L;

	public SiteletDescription getSitelet(Long id) {
		SiteletProperties item = SiteletProperties.get(id);
		return new SiteletDescription(item.entityTypeClassName, item.entityId);
	}
 
	public List<CrudNameIdPair> getSitelets(String containerId) {
		List<SiteletProperties> items = SiteletProperties
				.getByContainerId(containerId);
		List<CrudNameIdPair> result = new ArrayList<CrudNameIdPair>();
		for (SiteletProperties sitelet : items) {
			result.add(new CrudNameIdPair(sitelet.id, sitelet.entityTypeClassName));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private Model getSiteletEntity(SiteletProperties siteletProperties)
			throws ClassNotFoundException {
		Model m = Model.all(
				(Class<? extends Model>) Class
						.forName(siteletProperties.entityTypeClassName)).filter("id",
				siteletProperties.entityId).get();
		return m;
	}

	public void addSitelet(String containerName, String entityName,
			Long savedId) {

		// Get the next order int:
		Integer order = 0;
		List<SiteletProperties> items = SiteletProperties
				.getByContainerId(containerName);
		if (items != null && !items.isEmpty()) {
			SiteletProperties last = items.get(items.size() - 1);
			order = last.order + 1;
		}

		// insert the sitelet
		SiteletProperties item = new SiteletProperties();
		item.entityId = savedId;
		item.entityTypeClassName = entityName;
		item.order = order;
		item.containerId = containerName;
		item.insert();
		clearContainerCache(containerName);
	}

	public void saveSiteletOrder(String containerName, List<Long> siteletIds) {
		int order = 0;
		for (Long siteletId : siteletIds) {
			SiteletProperties item = SiteletProperties.get(siteletId);
			item.order = order;
			item.update();
			order++;
		}
		clearContainerCache(containerName);
	}

	public void deleteSitelets(String containerName, List<Long> ids) {
		for (Long id : ids) {
			SiteletProperties item = SiteletProperties.get(id);
			try {
				Model m = getSiteletEntity(item);
				if (m != null) {
					m.delete();
				}
			} catch (Exception e) {
				// expected when data model changes
				Logger.info("Trying to delete missing sitelet entity: "
						+ item.entityTypeClassName + " " + item.entityId);
			}
			item.delete();
		}
		clearContainerCache(containerName);
	}

	private void clearContainerCache(String containerName) {
		Cache.delete(SiteletHelper.CACHE_PREFIX + containerName);
	}

	public List<CrudEntityInfo> getSiteletTypes() {
		List<CrudEntityInfo> entities = new ArrayList<CrudEntityInfo>();
		for (Class<? extends Sitelet> clazz : Configurator.sitelets) {
			entities.add(new CrudEntityInfo(clazz.getName()));
		}
		return entities;
	}
	
	public void clearCache(Long entityId) {
		Cache.delete(SiteletProperties.getByEntityId(entityId).getCacheKey());
	}
}

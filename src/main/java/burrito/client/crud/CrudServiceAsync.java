package burrito.client.crud;

import java.util.List;

import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.generic.CrudEntityInfo;
import burrito.client.crud.generic.CrudEntityList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.kanal5.play.client.widgets.panels.table.PageMetaData;

/**
 * The asynchronous version of {@link CrudService}
 * 
 * @author henper
 * 
 */
public interface CrudServiceAsync {

	void describe(String entityName, Long id,
			Long copyFromId, AsyncCallback<CrudEntityDescription> callback);

	void save(CrudEntityDescription updateCrudEntityDescription,
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

}

package burrito.services;

import java.util.Date;
import java.util.List;

import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.PageMetaData;
import siena.Model;
/**
 * Common interface for search managers
 * 
 * Use {@link SearchManagerFactory}.getSearchManager() to gain accces to an implementation.
 * 
 * @author henper
 *
 */
public interface SearchManager {
	
	void insertOrUpdateSearchEntry(Model entity, Long entityId);

	void deleteSearchEntry(Class<? extends Model> ownerType, Long entityId);

	ItemCollection<SearchHit> search(Class<? extends Model> clazz, String query);

	ItemCollection<SearchHit> search(String query);

	ItemCollection<SearchHit> search(String query,
			PageMetaData<String> pageMetaData);

	ItemCollection<SearchHit> search(Class<? extends Model> clazz,
			String query, PageMetaData<String> page);

	List<SearchHit> getAllEntries();

	boolean clearIndexForEntity(Class<? extends Model> clazz);

	/**
	 * Gets the last modified date on the last updated entity search entry.
	 * 
	 * @return
	 */
	Date getLastModified(Class<? extends Model> clazz);
}

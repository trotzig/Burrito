package burrito.services;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;

import siena.Model;
import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.PageMetaData;
import burrito.util.StringUtils;


/**
 * This is the old type of SearchManager. It uses a SearchEntry entity in the datastore and uses the default datastore index to search. 
 * 
 * Ideas to this class come from
 * <nobr>http://googleappengine.blogspot
 * .com/2010/04/making-your-app-searchable-using-self.html</nobr>
 * 
 *  
 * @author henper
 *
 */
public class DatastoreSearchManager implements SearchManager {

	
	/** From StopAnalyzer Lucene 2.9.1 */
	public final static String[] stopWords = new String[] { "en", "ett", "i",
			"och", "eller", "men", "den", "det", "att" };

	@Override
	public void insertOrUpdateSearchEntry(Model entity, Long entityId) {
		boolean update = true;
		Class<? extends Model> ownerType = entity.getClass();
		
		SearchEntry entry = SearchEntry.getByOwner(ownerType, entityId);
		if (entry == null) {
			entry = new SearchEntry();
			entry.ownerClassName = ownerType.getName();
			entry.ownerId = entityId;
			entry.title = entity.toString();
			update = false;
		}
		Set<String> searchables = SearchServiceSearchManager.getSearchableTextsFromEntity(ownerType, entity);
		if (searchables.isEmpty()) {
			return;
		}
		String text = concatenateAndClean(searchables);
		entry.tokens = setToList(getTokensForIndexingOrQuery(text, 200));
		if (update) {
			entry.update();
		} else {
			entry.insert();
		}
	}

	@Override
	public void deleteSearchEntry(Class<? extends Model> ownerType, Long entityId) {
		SearchEntry entry = SearchEntry.getByOwner(ownerType, entityId);
		if (entry != null) entry.delete();
	}


	private List<String> setToList(Set<String> set) {
		List<String> list = new ArrayList<String>();
		for (String s : set) {
			list.add(s);
		}
		return list;
	}

	private String concatenateAndClean(Set<String> texts) {
		StringBuilder sb = new StringBuilder();
		for (String text : texts) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(StringUtils.stripHTML(text));
		}
		return sb.toString();
	}

	/**
	 * Uses english stemming (snowball + lucene) + stopwords for getting the
	 * words.
	 * 
	 * @param index
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Set<String> getTokensForIndexingOrQuery(String text,
			int maximumNumberOfTokensToReturn) {
		Set<String> returnSet = new HashSet<String>();
		try {
			Analyzer analyzer = new SnowballAnalyzer("Swedish", stopWords);
			TokenStream tokenStream = analyzer.tokenStream("content",
					new StringReader(text));
			Token token = new Token();
			while (((token = tokenStream.next()) != null)
					&& (returnSet.size() < maximumNumberOfTokensToReturn)) {
				returnSet.add(token.term());
			}

		} catch (IOException e) {
			throw new RuntimeException("Failed to get tokens from text", e);
		}

		return returnSet;

	}

	@Override
	public ItemCollection<SearchHit> search(String query) {
		return search(query, new PageMetaData<String>(100, 0, null, true));
	}
	@Override
	public ItemCollection<SearchHit> search(String query, PageMetaData<String> pageMetaData) {
		return search(null, query, pageMetaData);
	}

	@Override
	public ItemCollection<SearchHit> search(Class<? extends Model> clazz, String query, PageMetaData<String> page) {
		
		//Please note: Paging is not supported for this searchmanager type. Use the new SearchManager.class instead to be able to page through search results.
		
		Set<String> tokens = getTokensForIndexingOrQuery(StringUtils.stripHTML(query), 20);
		ItemCollection<SearchEntry> result = SearchEntry.search(clazz, tokens);
		ItemCollection<SearchHit> toReturn = new ItemCollection<SearchHit>(new ArrayList<SearchHit>(), result.isHasNextPage(), page.getPage(), page.getItemsPerPage());
		for (SearchEntry searchEntry : result) {
			toReturn.getItems().add(searchEntry.toSearchHit());
		}
		return toReturn;
	}

	@Override
	public ItemCollection<SearchHit> search(Class<? extends Model> clazz,
			String query) {
		return search(clazz, query, new PageMetaData<String>(100, 0, null, true));
	}

	@Override
	public List<SearchHit> getAllEntries() {
		ArrayList<SearchHit> result = new ArrayList<SearchHit>();
		for (SearchEntry entry : SearchEntry.all().fetch()) {
			result.add(entry.toSearchHit());
		}
		return result;
	}

	@Override
	public void clearIndexForEntity(Class<? extends Model> clazz) {
		SearchEntry.deleteAllForOwnerType(clazz);
	}
		
	

}

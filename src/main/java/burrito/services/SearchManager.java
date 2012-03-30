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

package burrito.services;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;

import siena.Model;
import burrito.annotations.SearchableField;
import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.PageMetaData;
import burrito.util.EntityUtil;
import burrito.util.StringUtils;


/**
 * Idéerna till den här klassen kommer främst från
 * <nobr>http://googleappengine.blogspot
 * .com/2010/04/making-your-app-searchable-using-self.html</nobr>
 * 
 * Teknikerna för säkning går ut på att splitta texten i tokens som sen sparas i
 * en entitet i databasen. App engines datastore sköter indexeringen och man kan
 * ställa enkla queries.
 * 
 * @author henper
 * 
 */
public class SearchManager {

	static SearchManager instance;

	public static SearchManager get() {
		if (instance == null) {
			instance = new SearchManager();
		}
		return instance;
	}

	
	private SearchManager() {
		//private
	}
	
	/** From StopAnalyzer Lucene 2.9.1 */
	public final static String[] stopWords = new String[] { "en", "ett", "i",
			"och", "eller", "men", "den", "det", "att" };

	public void insertOrUpdateSearchEntry(Model entity,
			Long entityId) {
		boolean update = true;
		Class<? extends Model> ownerType = entity.getClass();
		SearchEntry entry = SearchEntry.getByOwner(ownerType, entityId);
		if (entry == null) {
			entry = new SearchEntry();
			entry.ownerClassName = ownerType.getName();
			entry.ownerId = entityId;
			update = false;
		}
		Set<String> searchables = getSearchableTextsFromEntity(ownerType, entity);
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

	public void deleteSearchEntry(Model entity, Long entityId) {
		Class<? extends Model> ownerType = entity.getClass();
		SearchEntry entry = SearchEntry.getByOwner(ownerType, entityId);
		if (entry != null) entry.delete();
	}

	private Set<String> getSearchableTextsFromEntity(
			Class<? extends Model> ownerType, Model entity) {
		Set<String> searchables = new HashSet<String>();
		for (Field field : EntityUtil.getFields(ownerType)) {
			if (field.isAnnotationPresent(SearchableField.class)) {
				try {
					field.setAccessible(true);
					String text = (String) field.get(entity);
					if (text != null) {
						searchables.add(text);
					}
				} catch (Exception e) {
					throw new RuntimeException("Failed to get searchable texts from entity", e);
				}
			}
		}
		return searchables;
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

	/**
	 * Searches for entries matching a query.
	 * 
	 * @param clazz
	 * @param query
	 * @param p
	 * @return
	 */
	public ItemCollection<SearchEntry> search(Class<? extends Model> clazz,
			String query) {
		Set<String> tokens = getTokensForIndexingOrQuery(StringUtils
				.stripHTML(query), 20);
		return SearchEntry.search(clazz, tokens);
	}

}

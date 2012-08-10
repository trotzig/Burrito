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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;
import burrito.client.widgets.panels.table.ItemCollection;
/**
 * A datastore entity used for searching in combinmation with {@link DatastoreSearchManager}. 
 * Since App Engine version 1.7, search has been migrated to {@link SearchServiceSearchManager}.
 * 
 * @author henper
 *
 */
public class SearchEntry extends Model implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(Generator.AUTO_INCREMENT)
	public Long id;

	public List<String> tokens;

	public String ownerClassName;

	public Long ownerId;
	
	public String title;

	public static SearchEntry getByOwner(Class<? extends Model> ownerType,
			Long ownerId) {
		return all().filter("ownerClassName", ownerType.getName()).filter(
				"ownerId", ownerId).get();
	}

	public static Query<SearchEntry> all() {
		return Model.all(SearchEntry.class);
	}

	public static ItemCollection<SearchEntry> search(Class<? extends Model> clazz, Set<String> tokens) {
		if (tokens.size() == 1) {
			return searchStartsWith(clazz, tokens, 50);
		}
		
		Query<SearchEntry> query = all();
		if (clazz != null) {
			query.filter("ownerClassName", clazz.getName());
		}
		
		for (String token : tokens) {
			query.filter("tokens", token);
		}
						
		List<SearchEntry> entries = query.fetch();
		boolean hasNext = false;

		return new ItemCollection<SearchEntry>(entries, hasNext, 0, entries.size());
	}
	
	public static ItemCollection<SearchEntry> searchStartsWith(Class<? extends Model> clazz, Set<String> tokens, int limit) {
		Query<SearchEntry> query = all();
		if (clazz != null) {
			query.filter("ownerClassName", clazz.getName());
		}
		
		if (tokens.size() > 1) {
			for (String token : tokens) {
				query.filter("tokens", token);
			}
		} else if (tokens.size() == 1) {
			ArrayList<String> list = new ArrayList<String>(tokens);
			String lastItem = list.remove(0);
			
			query.filter("tokens >=", lastItem);
			query.filter("tokens <", lastItem + Character.MAX_VALUE);
		}
		
		query.limit(limit);
		List<SearchEntry> entries = query.fetch();
		boolean hasNext = false;

		return new ItemCollection<SearchEntry>(entries, hasNext, 0, entries.size());
	}

	public SearchHit toSearchHit() {
		SearchHit hit = new SearchHit();
		hit.setOwnerClass(ownerClassName);
		hit.setOwnerId(ownerId);
		hit.setTitle(title);
		hit.setSnippetHtml(null); //unable to snippet "old" search type
		return hit;
	}

	public static void deleteAllForOwnerType(Class<? extends Model> clazz) {
		all().filter("ownerClassName", clazz.getName()).delete();
	}

}

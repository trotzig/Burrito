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
import java.util.List;
import java.util.Set;

import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.PageMetaData;

import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;

public class SearchEntry extends Model implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(Generator.AUTO_INCREMENT)
	public Long id;

	public List<String> tokens;

	public String ownerClassName;

	public Long ownerId;

	public static SearchEntry getByOwner(Class<? extends Model> ownerType,
			Long ownerId) {
		return all().filter("ownerClassName", ownerType.getName()).filter(
				"ownerId", ownerId).get();
	}

	public static Query<SearchEntry> all() {
		return Model.all(SearchEntry.class);
	}

	public static ItemCollection<SearchEntry> search(
			Class<? extends Model> clazz, Set<String> tokens,
			PageMetaData<String> p) {
		Query<SearchEntry> query = all();
		query.filter("ownerClassName", clazz.getName());
		for (String token : tokens) {
			query.filter("tokens", token);
		}
		List<SearchEntry> entries = query.fetch(p.getItemsPerPage() + 1,
				(int) p.getRangeStart());
		boolean hasNext = false;
		if (entries.size() > p.getItemsPerPage()) {
			hasNext = true;
			entries.remove(entries.size() - 1);
		}
		return new ItemCollection<SearchEntry>(entries, hasNext, p.getPage(), p.getItemsPerPage());

	}
}

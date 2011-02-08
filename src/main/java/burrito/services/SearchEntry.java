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

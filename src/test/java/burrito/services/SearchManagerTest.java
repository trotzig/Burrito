package burrito.services;

import org.junit.Assert;
import org.junit.Test;

import siena.Query;
import burrito.client.widgets.panels.table.ItemCollection;
import burrito.test.TestBase;
import burrito.test.crud.SearchTestEntity;

public class SearchManagerTest extends TestBase {

	private SearchManager searchManager;

	@Override
	public void setUp() {
		super.setUp();
		
		SearchTestEntity entity = new SearchTestEntity();
		entity.setName("hello world");
		entity.save();
		
		searchManager = SearchManager.get();
		searchManager.insertOrUpdateSearchEntry(entity, 1L);
	}
	
	@Test
	public void search() {
		Query<SearchEntry> all = SearchEntry.all();
		Assert.assertEquals(1, all.count());
		
		ItemCollection<SearchEntry> search = searchManager.search(SearchTestEntity.class, "hello world");
		Assert.assertEquals(1, search.getItems().size());
	}
	
	@Test
	public void searchOnWrongKeyword() {
		ItemCollection<SearchEntry> search = searchManager.search(SearchTestEntity.class, "cake");
		Assert.assertEquals(0, search.getItems().size());
	}
	
	@Test
	public void searchOnWord() {
		ItemCollection<SearchEntry> search = searchManager.search(SearchTestEntity.class, "hello");
		Assert.assertEquals(1, search.getItems().size());
	}
	
	@Test
	public void searchOnPartOfWord() {
		ItemCollection<SearchEntry> search = searchManager.search(SearchTestEntity.class, "he");
		Assert.assertEquals("not possible today", 0, search.getItems().size());
	}
	
}

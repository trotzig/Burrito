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
		searchManager = SearchManager.get();
		
		SearchTestEntity entity = new SearchTestEntity();
		entity.setName("hello world");
		entity.save();
		searchManager.insertOrUpdateSearchEntry(entity, entity.getId());
		
		entity = new SearchTestEntity();
		entity.setName("pizza hamburger");
		entity.save();
		
		searchManager.insertOrUpdateSearchEntry(entity, entity.getId());
	}
	
	@Test
	public void weHaveSearchEntries() {
		Query<SearchEntry> all = SearchEntry.all();
		Assert.assertEquals(2, all.count());
		
		Object[] expecteds = {"hello","world"};
		Assert.assertArrayEquals(expecteds , all.get().tokens.toArray());
	}
	
	@Test
	public void search() {
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
	public void searchStartsWith() {
		ItemCollection<SearchEntry> search = searchManager.searchStartsWith(SearchTestEntity.class, "he");
		Assert.assertEquals(1, search.getItems().size());
	}
	
	@Test
	public void searchStartsWithWholeWord() {
		ItemCollection<SearchEntry> search = searchManager.searchStartsWith(SearchTestEntity.class, "hello");
		Assert.assertEquals(1, search.getItems().size());
	}
	
	@Test
	public void searchStartsWithOnlyTheFirstWord() {
		ItemCollection<SearchEntry> search = searchManager.searchStartsWith(SearchTestEntity.class, "hello wor");
		Assert.assertEquals(0, search.getItems().size());
		
		search = searchManager.searchStartsWith(SearchTestEntity.class, "hello world");
		Assert.assertEquals(1, search.getItems().size());
	}
	
}

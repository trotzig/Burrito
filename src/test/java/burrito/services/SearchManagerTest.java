package burrito.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.PageMetaData;
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
		List<SearchEntry> all = searchManager.getAllEntries();
		Assert.assertEquals(2, all.size());
		
		Assert.assertEquals(1, searchManager.search(SearchTestEntity.class, "world").getItems().size());
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
	public void searchOnFunction() {
		ItemCollection<SearchEntry> search = searchManager.search(SearchTestEntity.class, "searchable function hello");
		Assert.assertEquals(1, search.getItems().size());
	}
	
	
	@Test
	public void searchCanBeSortedAndPaged() {
		//Start by adding 10 entities to the database 
		int num = 10;
		for (int i = 0; i < num; i++) {
			SearchTestEntity entity = new SearchTestEntity();
			entity.setName("a " + i);
			entity.setDisplayableField("display " + (num - i - 1));
			entity.save();
			searchManager.insertOrUpdateSearchEntry(entity, entity.getId());
		}
		
		ItemCollection<SearchEntry> entries = searchManager.search(SearchTestEntity.class, "display", new PageMetaData<String>(num, 0, "displayableField", true));
		Assert.assertEquals("a 9", entries.getItems().get(0).getTitle());
		
		entries = searchManager.search(SearchTestEntity.class, "display", new PageMetaData<String>(2, 0, "displayableField", false));
		Assert.assertEquals("a 0", entries.getItems().get(0).getTitle());
		Assert.assertTrue(entries.isHasNextPage());
		
		entries = searchManager.search(SearchTestEntity.class, "display", new PageMetaData<String>(2, entries.getPage() + 1, "displayableField", false));
		Assert.assertEquals("a 2", entries.getItems().get(0).getTitle());
		
		
	}
	
	
}

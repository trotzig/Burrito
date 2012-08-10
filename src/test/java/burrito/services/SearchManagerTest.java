package burrito.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import burrito.Configurator;
import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.PageMetaData;
import burrito.test.TestBase;
import burrito.test.crud.ChildEntity;
import burrito.test.crud.NoSearchTestEntity;
import burrito.test.crud.SearchTestEntity;

public abstract class SearchManagerTest extends TestBase {

	protected SearchManager searchManager;

	@Before
	public void setupSearchManagerAndTestFixture() {
		Configurator.SEARCH_MANAGER_TYPE = getSearchManagerUnderTest();
		SearchManagerFactory.destroySearchManager();
		searchManager = SearchManagerFactory.getSearchManager();
		
		SearchTestEntity entity = new SearchTestEntity();
		entity.setName("hello world");
		entity.save();
		
		entity = new SearchTestEntity();
		entity.setName("pizza hamburger");
		entity.save();
		
	}
	
	
	protected abstract Class<? extends SearchManager> getSearchManagerUnderTest();

	@Test
	public void weHaveSearchEntries() {
		List<SearchHit> all = searchManager.getAllEntries();
		Assert.assertEquals(2, all.size());
		
		Assert.assertEquals(1, searchManager.search(SearchTestEntity.class, "world").getItems().size());
	}
	
	@Test
	public void search() {
		ItemCollection<SearchHit> search = searchManager.search(SearchTestEntity.class, "hello world");
		Assert.assertEquals(1, search.getItems().size());
	}
	
	@Test
	public void searchOnWrongKeyword() {
		ItemCollection<SearchHit> search = searchManager.search(SearchTestEntity.class, "cake");
		Assert.assertEquals(0, search.getItems().size());
	}
	
	@Test
	public void searchOnWord() {
		ItemCollection<SearchHit> search = searchManager.search(SearchTestEntity.class, "hello");
		Assert.assertEquals(1, search.getItems().size());
	}
	
	
	@Test
	public void searchOnFunction() {
		ItemCollection<SearchHit> search = searchManager.search(SearchTestEntity.class, "searchable function hello");
		Assert.assertEquals(1, search.getItems().size());
	}

	
	@Test
	public void searchIsDoneOnOneEntityType() {
		ChildEntity otherType = new ChildEntity();
		otherType.setChildProperty("hello");
		otherType.save();
		
		ItemCollection<SearchHit> search = searchManager.search(SearchTestEntity.class, "hello");
		Assert.assertEquals(1, search.getItems().size());
		
		search = searchManager.search(ChildEntity.class, "hello");
		Assert.assertEquals(1, search.getItems().size());
		
	}
	
	@Test
	public void searchCanBeSortedEvenIfValuesAreMissing() {
		ItemCollection<SearchHit> search = searchManager.search(SearchTestEntity.class, "display", new PageMetaData<String>(10, 0, "displayableField", true));
		Assert.assertEquals(0, search.getItems().size());
	}
	
	@Test
	public void searchFindsReturnedValuesFromDisplayableMethods() {
		ItemCollection<SearchHit> search = searchManager.search(SearchTestEntity.class, "unicorn");
		Assert.assertEquals(2, search.getItems().size());
	}
	
	
	@Test
	public void searchHasSameBehaviourAfterRebuiltIndex() {
		searchOnFunction();
		searchOnWord();
		searchOnWrongKeyword();
		
		new CrudServiceImpl().reindex(SearchTestEntity.class.getName(), new PageMetaData<String>(100, 0, null, false));
		
		searchOnFunction();
		searchOnWord();
		searchOnWrongKeyword();
	}
	
	@Test
	public void entitiesCanOverrideWhetherToBeSearchIndexedOrNot() {
		NoSearchTestEntity n = new NoSearchTestEntity();
		n.setName("yngwie");
		n.insert();
		
		ItemCollection<SearchHit> search = searchManager.search(NoSearchTestEntity.class, "yngwie");
		Assert.assertEquals(0, search.getItems().size());
		
	}
	
	
	
	
	@Test
	public void emptyEntityDoesNotCauseCrasch() {
		SearchTestEntity entity = new SearchTestEntity();
		entity.save();
	}
	
}

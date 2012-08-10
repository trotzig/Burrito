package burrito.services;

import org.junit.Assert;
import org.junit.Test;

import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.PageMetaData;
import burrito.test.crud.ChildEntity;
import burrito.test.crud.SearchTestEntity;

public class SearchServiceSearchManagerTest extends SearchManagerTest {

	@Override
	protected Class<? extends SearchManager> getSearchManagerUnderTest() {
		return SearchServiceSearchManager.class;
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
		}
		
		ItemCollection<SearchHit> entries = searchManager.search(SearchTestEntity.class, "display", new PageMetaData<String>(num, 0, "displayableField", true));
		Assert.assertEquals("a 9", entries.getItems().get(0).getTitle());
		
		entries = searchManager.search(SearchTestEntity.class, "display", new PageMetaData<String>(2, 0, "displayableField", false));
		Assert.assertEquals("a 0", entries.getItems().get(0).getTitle());
		Assert.assertTrue(entries.isHasNextPage());
		
		entries = searchManager.search(SearchTestEntity.class, "display", new PageMetaData<String>(2, entries.getPage() + 1, "displayableField", false));
		Assert.assertEquals("a 2", entries.getItems().get(0).getTitle());
		
		
	}
	

	@Test
	public void globalSearchReturnsDecentSnippets() {
		ChildEntity otherType = new ChildEntity();
		otherType.setChildProperty("I was at a big pile of something where I met a man who said hello and continued walking a long way back. Refrigerator.");
		otherType.save();
		
		ItemCollection<SearchHit> results = searchManager.search("hello");
		Assert.assertEquals(2, results.getItems().size());
		
		//Due to a current bug in the dev_appserver, snippets are missing. This led to our own snippet creator. 
		//It works, but will probably have to be updated soon. 
		Assert.assertEquals("...something where I met a man who said hello and <span class=\"snippet-highlight\">continued</span> walking a long way back....", searchManager.search("continued").getItems().get(0).getSnippetHtml());
		
	}

}

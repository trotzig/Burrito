package burrito.services;

import org.junit.Assert;
import org.junit.Test;

import burrito.client.widgets.panels.table.ItemCollection;
import burrito.test.crud.ChildEntity;

public class DatastoreSearchManagerTest extends SearchManagerTest {

	@Override
	protected Class<? extends SearchManager> getSearchManagerUnderTest() {
		return DatastoreSearchManager.class;
	}

	@Test
	public void assertCorrectSearchManager() {
		Assert.assertEquals(DatastoreSearchManager.class, SearchManagerFactory.getSearchManager().getClass());
	}
	
	

	@Test
	public void datastoreSearchManagerHasNoSupportForSnippets() {
		ChildEntity otherType = new ChildEntity();
		otherType.setChildProperty("I was at a big pile of something where I met a man who said hello and continued walking a long way back. Refrigerator.");
		otherType.save();
		
		ItemCollection<SearchHit> results = searchManager.search("hello");
		Assert.assertEquals(2, results.getItems().size());
		
		Assert.assertNull(searchManager.search("continued").getItems().get(0).getSnippetHtml());
		
	}
	
}

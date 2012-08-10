package burrito.controller;

import java.util.List;

import taco.Controller;
import burrito.services.SearchHit;
import burrito.services.SearchManagerFactory;
/**
 * Simple controller that performs a search and returns as an itemcollection
 * 
 * @author henper
 *
 */
public class GlobalSearchController implements Controller<List<SearchHit>> {

	private String query;
	
	@Override
	public List<SearchHit> execute() {
		return SearchManagerFactory.getSearchManager().search(query).getItems(); 
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	

}

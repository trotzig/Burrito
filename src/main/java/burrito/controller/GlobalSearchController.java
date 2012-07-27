package burrito.controller;

import java.util.List;

import taco.Controller;
import burrito.services.SearchEntry;
import burrito.services.SearchManager;
/**
 * Simple controller that performs a search and returns as an itemcollection
 * 
 * @author henper
 *
 */
public class GlobalSearchController implements Controller<List<SearchEntry>> {

	private String query;
	
	@Override
	public List<SearchEntry> execute() {
		return SearchManager.get().search(query).getItems(); 
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	

}

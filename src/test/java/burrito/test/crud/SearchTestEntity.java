package burrito.test.crud;

import burrito.BurritoModel;
import burrito.annotations.Displayable;
import burrito.annotations.SearchableField;
import burrito.annotations.SearchableMethod;

public class SearchTestEntity extends BurritoModel {
	
	@SearchableField
	private String name;

	private String notSearchField;
	
	@Displayable
	private String displayableField;
	
	public String getDisplayableField() {
		return displayableField;
	}

	public void setDisplayableField(String displayableField) {
		this.displayableField = displayableField;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setNotSearchField(String notSearchField) {
		this.notSearchField = notSearchField;
	}

	public String getNotSearchField() {
		return notSearchField;
	}
	
	@SearchableMethod
	public String searchableFunction() {
		return "searchable function " + name;
	}
	
	@Displayable
	public String displayableFunction() {
		return "unicorn";
	}
	
	@Override
	public String toString() {
		return name;
	}
}

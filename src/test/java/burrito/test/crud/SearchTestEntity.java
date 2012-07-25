package burrito.test.crud;

import siena.Generator;
import siena.Id;
import siena.Model;
import burrito.annotations.Displayable;
import burrito.annotations.SearchableField;
import burrito.annotations.SearchableMethod;

public class SearchTestEntity extends Model {

	@Id(Generator.AUTO_INCREMENT)
	private Long id;
	
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

	public Long getId() {
		return id;
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
	
	@Override
	public String toString() {
		return name;
	}
}

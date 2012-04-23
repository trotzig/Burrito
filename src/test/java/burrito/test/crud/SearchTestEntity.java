package burrito.test.crud;

import burrito.annotations.SearchableField;
import siena.Generator;
import siena.Id;
import siena.Model;

public class SearchTestEntity extends Model {

	@Id(Generator.AUTO_INCREMENT)
	private Long id;
	
	@SearchableField
	private String name;

	private String notSearchField;
	
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
}

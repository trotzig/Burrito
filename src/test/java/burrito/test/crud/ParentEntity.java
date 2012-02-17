package burrito.test.crud;

import siena.Generator;
import siena.Id;
import siena.core.lifecycle.PreInsert;
import siena.core.lifecycle.PreSave;
import siena.core.lifecycle.PreUpdate;
import burrito.annotations.AdminLink;
import burrito.annotations.Displayable;

public class ParentEntity extends GrandParentEntity {

	@Id(Generator.AUTO_INCREMENT)
	private Long id;
	
	private String parentProperty;
	

	public void setParentProperty(String parentProperty) {
		this.parentProperty = parentProperty;
	}

	public String getParentProperty() {
		return parentProperty;
	}
	
	public String parentMethod() {
		return "Not until Saturday!";
	}
	
	@Displayable
	public String displayableParentMethod() {
		return "I told you, not until the weekend!";
	}
	
	@AdminLink(text="Parent")
	public String adminLinkParentMethod() {
		return "/parent";
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	@PreInsert
	@PreSave
	@PreUpdate
	public void beforeSave() {
		parentProperty = "automatic";
	}
	
	
}

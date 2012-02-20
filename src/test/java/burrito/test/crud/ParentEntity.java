package burrito.test.crud;

import siena.Generator;
import siena.Id;
import siena.core.lifecycle.PreInsert;
import siena.core.lifecycle.PreSave;
import siena.core.lifecycle.PreUpdate;
import burrito.annotations.AdminLink;
import burrito.annotations.Displayable;
import burrito.annotations.Relation;

public class ParentEntity extends GrandParentEntity {

	@Id(Generator.AUTO_INCREMENT)
	private Long id;
	
	private Integer parentProperty;

	@Relation(ChildEntity.class)
	private Long someChildId;

	public void setParentProperty(Integer parentProperty) {
		this.parentProperty = parentProperty;
	}

	public Integer getParentProperty() {
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
		parentProperty = 123;
	}

	public void setSomeChildId(Long someChildId) {
		this.someChildId = someChildId;
	}

	public Long getSomeChildId() {
		return someChildId;
	}
	
	
}

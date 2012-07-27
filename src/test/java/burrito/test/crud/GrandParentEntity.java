package burrito.test.crud;

import siena.core.lifecycle.PreInsert;
import siena.core.lifecycle.PreSave;
import siena.core.lifecycle.PreUpdate;
import burrito.BurritoModel;
import burrito.annotations.AdminLink;
import burrito.annotations.Displayable;
import burrito.annotations.Required;
import burrito.annotations.SearchableField;
import burrito.annotations.Unique;

public class GrandParentEntity extends BurritoModel {
	
	@Displayable
	@SearchableField
	@Required
	private String grandParentProperty;

	@Unique
	private Long uniqueValue;

	
	public void setGrandParentProperty(String grandParentProperty) {
		this.grandParentProperty = grandParentProperty;
	}

	public String getGrandParentProperty() {
		return grandParentProperty;
	}

	public String grandParentMethod() {
		return "What?";
	}
	
	@Displayable
	public String displayableGrandParentMethod() {
		return "What?!";
	}
	

	@AdminLink(text="GrandParent")
	public String adminLinkGrandParentMethod() {
		return "/grand-parent";
	}
	
	@PreInsert
	@PreSave
	@PreUpdate
	public void beforeGrandparentSave() {
		grandParentProperty = "automatic";
	}

	public void setUniqueValue(Long uniqueValue) {
		this.uniqueValue = uniqueValue;
	}

	public Long getUniqueValue() {
		return uniqueValue;
	}
}

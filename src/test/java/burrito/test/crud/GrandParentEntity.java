package burrito.test.crud;

import burrito.annotations.AdminLink;
import burrito.annotations.Displayable;
import burrito.annotations.Required;
import burrito.annotations.SearchableField;
import siena.Model;
import siena.core.lifecycle.PreInsert;
import siena.core.lifecycle.PreSave;
import siena.core.lifecycle.PreUpdate;

public class GrandParentEntity extends Model {
	
	@Displayable
	@SearchableField
	@Required
	private String grandParentProperty;

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
}

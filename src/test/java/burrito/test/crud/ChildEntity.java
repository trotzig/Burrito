package burrito.test.crud;

import burrito.annotations.AdminLink;
import burrito.annotations.Displayable;

public class ChildEntity extends ParentEntity {

	@Displayable
	private String childProperty;

	public void setChildProperty(String childProperty) {
		this.childProperty = childProperty;
	}

	public String getChildProperty() {
		return childProperty;
	}
	
	
	public String childMethod() {
		return "I want candy!";
	}
	
	@Displayable
	public String displayableChildMethod() {
		return "but it's sooooo goooooooooood!";
	}
	

	@AdminLink(text="Child")
	public String adminLinkChildMethod() {
		return "/child";
	}
	
}

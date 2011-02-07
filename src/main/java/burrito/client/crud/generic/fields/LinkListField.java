package burrito.client.crud.generic.fields;

import java.util.List;

@SuppressWarnings("serial")
public class LinkListField extends StringListField {

	public LinkListField() {
		super();
	}
	
	public LinkListField(List<String> jsonLinks) {
		super(jsonLinks);
	}
	
}

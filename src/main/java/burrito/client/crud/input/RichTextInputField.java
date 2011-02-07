package burrito.client.crud.input;


import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.RichTextField;
import burrito.client.crud.widgets.CrudRichTextArea;

import com.google.gwt.user.client.ui.Widget;

public class RichTextInputField implements CrudInputField<String> {

	private RichTextField field;
	private CrudRichTextArea area;
	
	
	
	public RichTextInputField(RichTextField field) {
		this.field = field;
		this.area = new CrudRichTextArea((String) field.getValue());
	}

	public CrudField getCrudField() {
		field.setValue(area.getValue());
		return field;
	}

	public Widget getDisplayWidget() {
		return area;
	}

	public String getValue() {
		return area.getValue();
	}

	public void load(String value) {
		area.setValue(value);
	}

}

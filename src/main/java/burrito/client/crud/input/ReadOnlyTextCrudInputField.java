package burrito.client.crud.input;


import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.StringField;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ReadOnlyTextCrudInputField implements CrudInputField<String> {

	private StringField field;
	private Label label;

	public ReadOnlyTextCrudInputField(StringField field) {
		this.field = field;
		String value = (String) field.getValue();
		label = new Label(value != null ? value : "Not set");
	}

	@Override
	public CrudField getCrudField() {
		return field;
	}

	@Override
	public Widget getDisplayWidget() {
		return label;
	}

	@Override
	public String getValue() {
		return (String) field.getValue();
	}

	@Override
	public void load(String value) {
	}
}

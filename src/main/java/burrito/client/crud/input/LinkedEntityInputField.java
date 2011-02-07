package burrito.client.crud.input;


import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.LinkedEntityField;
import burrito.client.crud.widgets.LinkedEntityWidget;

import com.google.gwt.user.client.ui.Widget;

public class LinkedEntityInputField implements CrudInputField<String> {

	private LinkedEntityField field;
	private LinkedEntityWidget widget;

	public LinkedEntityInputField(LinkedEntityField field) {
		this.field = field;
		widget = new LinkedEntityWidget(field.isRequired(), false);
		load((String) field.getValue());
	}

	public CrudField getCrudField() {
		field.setValue(getValue());
		return field;
	}

	public Widget getDisplayWidget() {
		return widget;
	}

	public String getValue() {
		return widget.getValue();
	}

	public void load(String json) {
		widget.setValue(json);
	}

}

package burrito.client.crud.input;

import java.util.List;



import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.LinkListField;
import burrito.client.crud.widgets.LinkedEntityWidget;

import com.google.gwt.user.client.ui.Widget;

public class LinkListInputField implements CrudInputField<List<String>> {

	private LinkListField field;
	private LinkedEntityWidget widget;

	@SuppressWarnings("unchecked")
	public LinkListInputField(LinkListField field) {
		this.field = field;
		widget = new LinkedEntityWidget(field.isRequired(), true);
		load((List<String>) field.getValue());
	}

	public CrudField getCrudField() {
		field.setValue(getValue());
		return field;
	}

	public Widget getDisplayWidget() {
		return widget;
	}

	public List<String> getValue() {
		return widget.getValues();
	}

	public void load(List<String> value) {
		widget.setValues(value);
	}

}

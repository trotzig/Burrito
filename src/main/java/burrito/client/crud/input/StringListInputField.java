package burrito.client.crud.input;

import java.util.List;



import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.StringListField;
import burrito.client.crud.widgets.StringListWidget;

import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("rawtypes")
public class StringListInputField implements CrudInputField {

	private StringListWidget stringListWidget;
	private StringListField field;
	
	@SuppressWarnings("unchecked")
	public StringListInputField(StringListField field) {
		this.field = field;
		stringListWidget = new StringListWidget((List<String>) field.getValue());
	}

	public CrudField getCrudField() {
		field.setValue(getValue());
		return field;
	}

	public Widget getDisplayWidget() {
		return stringListWidget;
	}

	public Object getValue() {
		return stringListWidget.getValue();
	}

	@SuppressWarnings("unchecked")
	public void load(Object value) {
		stringListWidget.setValue((List<String>) value);
	}

}

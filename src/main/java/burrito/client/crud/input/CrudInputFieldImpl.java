package burrito.client.crud.input;


import burrito.client.crud.generic.CrudField;
import burrito.client.widgets.inputfield.InputField;

import com.google.gwt.user.client.ui.Widget;

public class CrudInputFieldImpl<T> implements CrudInputField<T> {

	private InputField<T> widget;
	private CrudField field;

	public CrudInputFieldImpl(CrudField field, InputField<T> widget, T value) {
		this.widget = widget;
		this.field = field;
		load(value);
	}
	
	public Widget getDisplayWidget() {
		return widget;
	}

	public T getValue() {
		return widget.getValue();
	}

	public void load(T value) {
		widget.setValue(value);		
	}
	
	public CrudField getCrudField() {
		field.setValue(getValue());
		return field;
	}

}

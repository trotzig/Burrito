package burrito.client.crud.input;

import java.util.ArrayList;
import java.util.List;

import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.IntegerListField;
import burrito.client.crud.widgets.StringListWidget;

import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("rawtypes")
public class IntegerListInputField implements CrudInputField {

	private StringListWidget stringListWidget;
	private final IntegerListField field;

	public IntegerListInputField(IntegerListField field) {
		this.field = field;
		
		List<Integer> values = (List<Integer>) field.getValue();
		if (values == null) {
			values = new ArrayList<Integer>();
		}
		
		List<String> listString = new ArrayList<String>();
		for (Integer value : values) {
			listString.add(value.toString());
		}
	
		stringListWidget = new StringListWidget(listString);
	}

	@Override
	public Widget getDisplayWidget() {
		return stringListWidget;
	}

	@Override
	public void load(Object value) {
		List<Integer> values = (List<Integer>) value;
		if (values == null) {
			values = new ArrayList<Integer>();
		}
		
		List<String> listString = new ArrayList<String>();
		for (Integer integer : values) {
			listString.add(integer.toString());
		}
		stringListWidget.setValue(listString);
	}

	@Override
	public Object getValue() {
		List<String> values = stringListWidget.getValue();
		if (values == null) {
			return new ArrayList<Integer>();
		}
		
		List<Integer> integers = new ArrayList<Integer>();
		for (String value : values) {
			int parseInt = Integer.parseInt(value);
			integers.add(parseInt);
		}
		
		return integers;
	}

	@Override
	public CrudField getCrudField() {
		field.setValue(getValue());
		return field;
	}

}

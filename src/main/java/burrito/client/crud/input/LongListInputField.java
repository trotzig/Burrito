package burrito.client.crud.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.LongListField;
import burrito.client.crud.widgets.StringListWidget;

@SuppressWarnings("rawtypes")
public class LongListInputField implements CrudInputField {

	private StringListWidget stringListWidget;
	private final LongListField field;

	public LongListInputField(LongListField field) {
		this.field = field;
		List<Long> values = (List<Long>) field.getValue();
		if (values == null) {
			values = new ArrayList<Long>();
		}
		
		List<String> listString = new ArrayList<String>();
		for (Long value : values) {
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
		List<Long> values = (List<Long>) value;
		if (values == null) {
			values = new ArrayList<Long>();
		}
		
		List<String> listString = new ArrayList<String>();
		for (Long longValue : values) {
			listString.add(longValue.toString());
		}
		
		stringListWidget.setValue(listString);
	}

	@Override
	public Object getValue() {
		List<String> values = stringListWidget.getValue();
		if (values == null) {
			return new ArrayList<Long>();
		}
		
		List<Long> longs = new ArrayList<Long>();
		for (String value : values) {
			Long parseLong = Long.parseLong(value);
			longs.add(parseLong);
		}
		
		return longs;
	}

	@Override
	public CrudField getCrudField() {
		field.setValue(getValue());
		return field;
	}

}

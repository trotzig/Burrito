package burrito.client.crud.generic.fields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import burrito.client.crud.generic.CrudField;

@SuppressWarnings("serial")
public class StringSelectionField extends CrudField {

	private Map<String, String> nameDisplay;
	private String value;

	public StringSelectionField(String value, String[] list) {
		nameDisplay = new HashMap<String, String>();
		for (int i = 0; i < list.length; i++) {
			String v = list[i];
			String d = list[++i];
			nameDisplay.put(v, d);
		}
		String display = nameDisplay.get(value);
		if (display != null) {
			// ensure that old value is still valid by checking the list of
			// possible values
			this.value = value;
		}
	}

	public StringSelectionField() {
		// default constructor
	}

	@Override
	public Class<?> getType() {
		return String.class;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = (String) value;
	}

	public String getLabel(String obj) {
		return nameDisplay.get(obj);
	}

	public List<String> getValues() {
		List<String> result = new ArrayList<String>();
		for (String string : nameDisplay.keySet()) {
			result.add(string);
		}
		return result;
	}

}

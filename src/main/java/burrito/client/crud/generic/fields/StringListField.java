package burrito.client.crud.generic.fields;

import java.util.List;

import burrito.client.crud.generic.CrudField;

@SuppressWarnings("serial")
public class StringListField extends CrudField {

	private List<String> strings;

	public StringListField() {
		// Default constructor
	}

	public StringListField(List<String> strings) {
		this.strings = strings;
	}

	@Override
	public Class<?> getType() {
		return List.class;
	}

	@Override
	public Object getValue() {
		return strings;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.strings = (List<String>) value;
	}

}

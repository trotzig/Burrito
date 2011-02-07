package burrito.client.crud.generic.fields;

import burrito.client.crud.generic.CrudField;

public class DisplayableMethodField extends CrudField {

	private static final long serialVersionUID = 1L;

	private String value;

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

	@Override
	public boolean isSortable() {
		return false;
	}
}

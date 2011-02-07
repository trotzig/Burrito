package burrito.client.crud.generic.fields;

import burrito.client.crud.generic.CrudField;


public class BooleanField extends CrudField {

	private static final long serialVersionUID = 1L;
	private Boolean bool;

	public BooleanField() {
		// default constructor
	}

	public BooleanField(Boolean bool) {
		super();
		this.bool = bool;
	}

	@Override
	public Class<?> getType() {
		return Boolean.class;
	}

	@Override
	public Object getValue() {
		return bool;
	}

	@Override
	public void setValue(Object value) {
		this.bool = (Boolean) value;
	}
}

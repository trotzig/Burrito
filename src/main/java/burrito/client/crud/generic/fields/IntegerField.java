package burrito.client.crud.generic.fields;

import burrito.client.crud.generic.CrudField;




/**
 * A {@link CrudField} representating an Integer
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class IntegerField extends CrudField {

	private Integer value;

	public IntegerField() {
		// default constructor
	}

	public IntegerField(Integer integer) {
		this.value = integer;
	}

	@Override
	public Class<?> getType() {
		return Integer.class;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = (Integer) value;
	}

}

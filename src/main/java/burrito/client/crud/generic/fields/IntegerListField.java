package burrito.client.crud.generic.fields;

import java.util.List;

import burrito.client.crud.generic.CrudField;

@SuppressWarnings("serial")
public class IntegerListField extends CrudField {

	private List<Integer> integers;

	public IntegerListField(List<Integer> integers) {
		this.integers = integers;
	}

	public IntegerListField() {
	}
	
	@Override
	public Object getValue() {
		return this.integers;
	}

	@Override
	public Class<?> getType() {
		return List.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.integers = (List<Integer>) value;
	}

}

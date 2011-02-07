package burrito.client.crud.generic.fields;

import java.io.Serializable;

import burrito.client.crud.generic.CrudField;




@SuppressWarnings("serial")
public class EnumField extends CrudField implements Serializable {

	private String value;
	private String typeClassName;

	public EnumField(String value, String typeClassName) {
		this.value = value;
		this.typeClassName = typeClassName;
	}

	public EnumField() {
		// Default constructor
	}

	@Override
	public Class<?> getType() {
		return Enum.class;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = (String) value;
	}
	
	public void setTypeClassName(String typeClassName) {
		this.typeClassName = typeClassName;
	}
	
	public String getTypeClassName() {
		return typeClassName;
	}

}

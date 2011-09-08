package burrito.client.crud.generic.fields;

import java.util.List;

import burrito.client.crud.generic.CrudField;

@SuppressWarnings("serial")
public class LongListField extends CrudField {

	private List<Long> longs;
	
	public LongListField(List<Long> longs) {
		this.longs = longs;
	}
	
	public LongListField() {
	}
	
	@Override
	public Object getValue() {
		return longs;
	}

	@Override
	public Class<?> getType() {
		return List.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.longs = (List<Long>) value;
	}

}

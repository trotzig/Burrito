package burrito.client.crud.generic.fields;

import burrito.client.crud.generic.CrudField;

@SuppressWarnings("serial")
public class ManyToOneRelationField extends CrudField {

	private Long id;
	private String relatedEntityName;

	public ManyToOneRelationField(Long id, String relatedEntityName) {
		this.id = id;
		this.relatedEntityName = relatedEntityName;
	}

	public ManyToOneRelationField() {
		// default constructor, needed by GWT
	}

	@Override
	public Class<?> getType() {
		return Long.class;
	}

	@Override
	public Object getValue() {
		return id;
	}

	@Override
	public void setValue(Object value) {
		this.id = (Long) value;
	}

	public String getRelatedEntityName() {
		return relatedEntityName;
	}

	public void setRelatedEntityName(String relatedEntityName) {
		this.relatedEntityName = relatedEntityName;
	}

}

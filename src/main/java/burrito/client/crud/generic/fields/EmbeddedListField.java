package burrito.client.crud.generic.fields;

import java.util.List;

import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.generic.CrudField;




public class EmbeddedListField extends CrudField {

	private static final long serialVersionUID = 1L;

	private List<CrudEntityDescription> embedded;
	private String embeddedClassName;
	
	
	public EmbeddedListField(List<CrudEntityDescription> embedded, String embeddedClassName) {
		this.embedded = embedded;
		this.embeddedClassName = embeddedClassName;
	}
	
	public EmbeddedListField() {
		//default constructor
	}
	
	public String getEmbeddedClassName() {
		return embeddedClassName;
	}
	
	@Override
	public Class<?> getType() {
		return List.class;
	}

	@Override
	public Object getValue() {
		return embedded;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.embedded = (List<CrudEntityDescription>) value;
	}

}

package burrito.client.crud.generic.fields;

import burrito.client.crud.generic.CrudField;

public class StringField extends CrudField {

	private static final long serialVersionUID = 1L;

	private String string;
	private boolean readOnly = false;
	private String regexpPattern;
	private String regexpDescription;
	private boolean renderAsTextArea = false;
	
	public StringField() {
		// Default constructor
	}
	
	public StringField(String string) {
		super();
		this.string = string;
	}
	
	@Override
	public Class<?> getType() {
		return String.class;
	}
	
	@Override
	public Object getValue() {
		return string;
	}
	
	@Override
	public void setValue(Object value) {
		this.string = (String) value;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void setRegexpPattern(String regexpPattern) {
		this.regexpPattern = regexpPattern;
	}

	public String getRegexpPattern() {
		return regexpPattern;
	}

	public void setRegexpDescription(String regexpDescription) {
		this.regexpDescription = regexpDescription;
	}

	public String getRegexpDescription() {
		return regexpDescription;
	}
	
	public void setRenderAsTextArea(boolean renderAsTextArea) {
		this.renderAsTextArea = renderAsTextArea;
	}
	
	public boolean isRenderAsTextArea() {
		return renderAsTextArea;
	}
}

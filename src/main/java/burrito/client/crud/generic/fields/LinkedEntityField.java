package burrito.client.crud.generic.fields;

@SuppressWarnings("serial")
public class LinkedEntityField extends StringField {

	/**
	 * Creates a new field with a json string
	 * 
	 * @param json
	 */
	public LinkedEntityField(String json) {
		super(json);
	}

	public LinkedEntityField() {
		// default, empty constructor
	}

}

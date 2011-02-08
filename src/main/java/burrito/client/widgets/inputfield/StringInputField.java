package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.InputField;

public class StringInputField extends InputField<String> {

	public StringInputField(boolean required) {
		super(required);
	}

	@Override
	protected String parseValue(String text) {
		// all values are ok
		return text;
	}

}

package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.InputField;
import burrito.client.widgets.validation.IntegerValidator;


public class IntegerInputField extends InputField<Integer> {

	public IntegerInputField(boolean required) {
		super(required);
		addInputFieldValidator(new IntegerValidator());
	}

	@Override
	protected Integer parseValue(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}

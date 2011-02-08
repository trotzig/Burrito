package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.InputField;
import burrito.client.widgets.validation.LongValidator;


public class LongInputField extends InputField<Long> {

	public LongInputField(boolean required) {
		super(required);
		addInputFieldValidator(new LongValidator());
	}

	@Override
	protected Long parseValue(String text) {
		try {
			Long value = Long.parseLong(text);
			return value;
		} catch (NumberFormatException e) {
			return null;
		}
	}

}

package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.InputField;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

public class PositiveIntegerInputField extends InputField<Integer> {

	private CommonMessages messages = GWT.create(CommonMessages.class);

	public PositiveIntegerInputField(boolean required) {
		super(required);
		addInputFieldValidator(new InputFieldValidator() {

			@Override
			public void validate(String value) throws ValidationException {
				Integer i = parseValue(value);
				if (i == null) {
					throw new ValidationException(messages
							.validationPositiveNumber());
				}
			}
		});
	}

	@Override
	protected Integer parseValue(String text) {
		try {
			Integer i = Integer.parseInt(text);
			if (i > 0) {
				return i;
			}
			return null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

}

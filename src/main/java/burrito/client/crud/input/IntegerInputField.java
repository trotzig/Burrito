package burrito.client.crud.input;

import burrito.client.widgets.inputfield.InputField;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

public class IntegerInputField extends InputField<Integer> {
	private CommonMessages messages = GWT.create(CommonMessages.class);

	public IntegerInputField(boolean required) {
		super(required);
		addInputFieldValidator(new InputFieldValidator() {
			
			@Override
			public void validate(String value) throws ValidationException {
				Integer i = parseValue(value);
				if (i == null) {
					throw new ValidationException(messages.validationNotNumber());
				}
			}
		});
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

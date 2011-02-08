package burrito.client.crud.widgets;

import burrito.client.widgets.inputfield.InputField;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

public class PositiveLongInputField extends InputField<Long> {


	private CommonMessages messages = GWT.create(CommonMessages.class);

	public PositiveLongInputField(boolean required) {
		super(required);
		addInputFieldValidator(new InputFieldValidator() {

			public void validate(String value) throws ValidationException {
				Long i = parseValue(value);
				if (i == null) {
					throw new ValidationException(messages
							.validationPositiveNumber());
				}
			}
		});
	}

	@Override
	protected Long parseValue(String text) {
		try {
			Long i = Long.parseLong(text);
			if (i > 0) {
				return i;
			}
			return null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

}

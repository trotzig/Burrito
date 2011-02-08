package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.PositiveIntegerInputField;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

public class YearInputField extends PositiveIntegerInputField {

	private CommonMessages messages = GWT.create(CommonMessages.class);

	public YearInputField(boolean required) {
		super(required);
		addInputFieldValidator(new InputFieldValidator() {

			@Override
			public void validate(String value) throws ValidationException {
				Integer i = parseValue(value);
				if (i.intValue() < 1900 || i.intValue() > 2999) {
					throw new ValidationException(messages
							.validationInvalidBirthYear());
				}
			}
		});
	}

}

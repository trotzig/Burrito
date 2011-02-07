package burrito.client.crud.input;

import com.google.gwt.core.client.GWT;
import com.kanal5.play.client.widgets.inputfield.InputField;
import com.kanal5.play.client.widgets.messages.CommonMessages;
import com.kanal5.play.client.widgets.validation.InputFieldValidator;
import com.kanal5.play.client.widgets.validation.ValidationException;

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

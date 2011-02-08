package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.InputField;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;


public class RelativeURLInputField extends InputField<String> {
	private CommonMessages messages = GWT.create(CommonMessages.class);
	public RelativeURLInputField(boolean required) {
		super(required);
		addInputFieldValidator(new InputFieldValidator() {
			
			@Override
			public void validate(String value) throws ValidationException {		
				if (!value.startsWith("/")) {
					throw new ValidationException(messages.mailURLErrorValidateMessage());
				}
				
			}
		});
	}	

	@Override
	protected String parseValue(String text) {
		return text;
	}
	
}

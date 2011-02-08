package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.StringInputField;
import burrito.client.widgets.inputfield.ValidationMessages;
import burrito.client.widgets.newpassword.PasswordValidator;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class PasswordInputField extends StringInputField {

	private PasswordValidator validator = new PasswordValidator();
	private ValidationMessages messages = GWT.create(ValidationMessages.class);
	public PasswordInputField(boolean required) {
		super(required);
		addInputFieldValidator(new InputFieldValidator() {
		
			@Override
			public void validate(String password) throws ValidationException {
				if (!validator.isValid(password)) {
					throw new ValidationException(messages.validatePassword());
				}
			}
		});
	}
	
	@Override
	protected TextBox createField() {
		return new PasswordTextBox();
	}

}

package burrito.client.widgets.inputfield;

import burrito.client.widgets.inputfield.StringInputField;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

/**
 * Input field with validation for tags. Only lowercase letters a-ö, numbers and
 * space characters are allowed.
 * 
 * @author henper
 * 
 */
public class TagInputField extends StringInputField {

	private static String regexp = "[0-9' a-zåäö-]+";
	private CommonMessages messages = GWT.create(CommonMessages.class);
	
	
	public TagInputField(boolean required) {
		super(required);
		addInputFieldValidator(new InputFieldValidator() {

			@Override
			public void validate(String value) throws ValidationException {
				if (value == null) {
					// this is taken care of by the required validator.
					return;
				}
				if (!value.matches(regexp)) {
					throw new ValidationException(messages.validationTags());
				}
			}
		});
	}

}

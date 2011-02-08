package burrito.client.widgets.inputfield;

import com.google.gwt.i18n.client.Messages;

public interface ValidationMessages extends Messages {

	String validateNotNegativeNumber();

	String validateURL();

	String validatePassword();

	String displayNameUnique();

	String displayNameNotUnique();

	String displayNameInvalid();

	String displayNameTooLong();

	String colorNotValid();

	String invalidAdPackage();
	
}

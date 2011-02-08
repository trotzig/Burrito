package burrito.client.widgets.messages;

import com.google.gwt.i18n.client.Messages;

public interface CommonMessages extends Messages {

	String rpcFailure(String message, String className);

	String validationNotNumber();
	
	String validationPositiveNumber();

	String validationRequiredField();

	String loadingPortletConfiguration();

	String save();

	String configurationUpdated();

	String mailURLErrorValidateMessage();

	String validationInvalidEmail();

	String validationInvalidBirthYear();

	String selectSex();
	
	String selectBirthYear();

	String selectTown();
	
	String sexFemale();

	String sexMale();

	String validationTags();

	String videoSelectorSearch();

	String clearSelectedVideo();

	String confirm();

	String confirmFacebookLogout();

	String confirmFacebookLogoutYes();

	String confirmFacebookLogoutNo();

	String confirmFacebookLogoutCancel();

	String blobImageRequiredSize(int requiredWidth, int requiredHeight);

	String blobImageRequiredWidth(int requiredWidth);

	String blobImageRequiredHeight(int requiredHeight);

	String blobImageAnySize();

	String validationTooLong(int maxLength);

	String blobImageChange();

	String add();
}

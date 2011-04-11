/**
 * Copyright 2011 Henric Persson (henric.persson@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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

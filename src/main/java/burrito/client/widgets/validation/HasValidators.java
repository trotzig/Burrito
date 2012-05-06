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

package burrito.client.widgets.validation;

import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

public interface HasValidators {

	/**
	 * Adds a validator
	 * 
	 * @param validator
	 */
	void addInputFieldValidator(InputFieldValidator validator);

	/**
	 * Validates against all added validators
	 * 
	 * @throws ValidationException
	 */
	boolean validate();

	/**
	 * Manually sets a validation error
	 * 
	 * @param validationError
	 */
	void setValidationError(String validationError);

	/**
	 * Called when there is a validation error. Should focus the widget holding the error. 
	 */
	void highlight();

}

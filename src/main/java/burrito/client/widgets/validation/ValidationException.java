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

import burrito.client.widgets.validation.ValidationException;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	private String validationMessage;

	/**
	 * Constructs a new {@link ValidationException} with a specified
	 * validationMessage. This message must be safe to display to a user. 
	 * <br/>
	 * Good example: "Please enter a number between 1 to 99"
	 * <br/>
	 * Bad example (avoid): "Not a valid integer"
	 * 
	 * @param validationMessage
	 */
	public ValidationException(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	/**
	 * Gets a message that describes the validation error
	 * 
	 * @return
	 */
	public String getValidationMessage() {
		return validationMessage;
	}

}

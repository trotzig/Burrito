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

package burrito.client.crud.input;


import burrito.client.crud.generic.CrudField;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ReadOnlyTextCrudInputField implements CrudInputField<String> {

	private CrudField field;
	private Label label;

	public ReadOnlyTextCrudInputField(CrudField field) {
		this.field = field;
		
		String value = null;
		
		//Perhaps add a getReadOnlyHTML() method on CrudField? 
		//That way we would get better outputs than just toString()
		
		Object objectValue = field.getValue();
		if (objectValue != null) {
			value = objectValue.toString();
		}
		
		label = new Label(value != null ? value : "(Not set)");
		label.addStyleName("k5-ReadOnlyTextCrudInputField");
		label.addStyleName("readOnly");
	}

	@Override
	public CrudField getCrudField() {
		return field;
	}

	@Override
	public Widget getDisplayWidget() {
		return label;
	}

	@Override
	public String getValue() {
		return field.toString();
	}

	@Override
	public void load(String value) {
	}
}

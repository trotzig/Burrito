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
import burrito.client.crud.generic.fields.LinkedEntityField;
import burrito.client.crud.widgets.LinkedEntityWidget;

import com.google.gwt.user.client.ui.Widget;

public class LinkedEntityInputField implements CrudInputField<String> {

	private LinkedEntityField field;
	private LinkedEntityWidget widget;

	public LinkedEntityInputField(LinkedEntityField field) {
		this.field = field;
		widget = new LinkedEntityWidget(field.isRequired(), false);
		load((String) field.getValue());
	}

	public CrudField getCrudField() {
		field.setValue(getValue());
		return field;
	}

	public Widget getDisplayWidget() {
		return widget;
	}

	public String getValue() {
		return widget.getValue();
	}

	public void load(String json) {
		widget.setValue(json);
	}

}

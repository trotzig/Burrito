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
import burrito.client.widgets.inputfield.InputField;

import com.google.gwt.user.client.ui.Widget;

public class CrudInputFieldImpl<T> implements CrudInputField<T> {

	private InputField<T> widget;
	private CrudField field;

	public CrudInputFieldImpl(CrudField field, InputField<T> widget, T value) {
		this.widget = widget;
		this.field = field;
		load(value);
	}
	
	public Widget getDisplayWidget() {
		return widget;
	}

	public T getValue() {
		return widget.getValue();
	}

	public void load(T value) {
		widget.setValue(value);		
	}
	
	public CrudField getCrudField() {
		field.setValue(getValue());
		return field;
	}

}

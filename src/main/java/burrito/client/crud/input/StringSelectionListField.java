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
import burrito.client.crud.generic.fields.StringSelectionField;

import com.google.gwt.user.client.ui.Widget;
import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;

@SuppressWarnings("rawtypes")
public class StringSelectionListField implements CrudInputField {

	private StringSelectionField field;
	private SelectionList<String> selectionList;

	public StringSelectionListField(final StringSelectionField field) {
		this.field = field;
		this.selectionList = new SelectionList<String>(field.isRequired());
		this.selectionList.setNullSelectLabel("");
		this.selectionList
				.setLabelCreator(new SelectionListLabelCreator<String>() {

					@Override
					public String createLabel(String obj) {
						return field.getLabel(obj);
					}
				});
		load(field.getValue());
		selectionList.setModel(field.getValues());
		selectionList.render();

	}

	public CrudField getCrudField() {
		field.setValue(selectionList.getValue());
		return field;
	}

	public Widget getDisplayWidget() {
		return selectionList;
	}

	public Object getValue() {
		return selectionList.getValue();
	}

	public void load(Object value) {
		selectionList.setValue((String) value);
	}

}

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

import java.util.List;



import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.ListedByEnumField;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("rawtypes")
public class ListedByEnumSelectionListField implements CrudInputField {

	private static CrudServiceAsync service = GWT.create(CrudService.class);

	private SelectionList<String> selectionList;

	private ListedByEnumField field;

	public ListedByEnumSelectionListField(final ListedByEnumField field) {
		this.field = field;
		selectionList = new SelectionList<String>(field.isRequired());
		selectionList.setNullSelectLabel("");
		selectionList.setLabelCreator(new SelectionListLabelCreator<String>() {

			@Override
			public String createLabel(String obj) {
				return CrudLabelHelper.getString((field.getTypeClassName()
						+ "_" + obj.toString()).replace('.', '_'));
			}
		});
		load(field.getValue());
		init();
	}

	private void init() {
		service.getEnumListValues(field.getTypeClassName(),
				new AsyncCallback<List<String>>() {

					public void onSuccess(List<String> result) {
						selectionList.setModel(result);
						selectionList.render();
					}

					public void onFailure(Throwable caught) {
						throw new RuntimeException(
								"Failed to get list values of type "
										+ field.getType(), caught);
					}
				});
	}

	public CrudField getCrudField() {
		field.setValue(getValue());
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

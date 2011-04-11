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



import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.ManyToManyRelationField;
import burrito.client.crud.widgets.MultiValueListBox;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import burrito.client.widgets.form.EditForm;

@SuppressWarnings("rawtypes")
public class ListCrudInputField implements CrudInputField {
	
	
	private class MultiValueListBoxWrapper extends Composite implements HasChangeHandlers {
		private VerticalPanel wrapper = new VerticalPanel();
		
		public MultiValueListBoxWrapper() {
			wrapper.add(listBox);
			wrapper.add(new RelatedEntityEditAnchor(field.getRelatedEntityName(), new EditForm.SaveCancelListener() {
				
				public void onSave() {
					listBox.load();
				}
				
				public void onPartialSave(String warning) {
					//do nothing
				}
				
				public void onCancel() {
					//do nothing
				}
			}));
			initWidget(wrapper);
		}

		public HandlerRegistration addChangeHandler(ChangeHandler handler) {
			return listBox.addChangeHandler(handler);
		}
	}
	private ManyToManyRelationField field;
	private MultiValueListBox listBox;
	private MultiValueListBoxWrapper wrapper;
	
	@SuppressWarnings("unchecked")
	public ListCrudInputField(ManyToManyRelationField field) {
		this.field = field;
		this.listBox = new MultiValueListBox(field.getRelatedEntityName());
		listBox.setSelectedValues((List<Long>) field.getValue());
		wrapper = new MultiValueListBoxWrapper();
	}

	public CrudField getCrudField() {
		field.setValue(listBox.getSelectedValues());
		return field;
	}

	public Widget getDisplayWidget() {
		return wrapper;
	}

	public Object getValue() {
		return listBox.getSelectedValues();
	}

	@SuppressWarnings("unchecked")
	public void load(Object value) {
		listBox.setSelectedValues((List<Long>) value);
	}

}

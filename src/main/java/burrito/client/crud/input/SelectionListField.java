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


import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.ManyToOneRelationField;
import burrito.client.crud.widgets.RelationSelectionList;
import burrito.client.widgets.validation.HasValidators;
import burrito.client.widgets.validation.InputFieldValidator;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import burrito.client.widgets.form.EditForm;

@SuppressWarnings("rawtypes")
public class SelectionListField implements CrudInputField {
	
	class RelationSelectionListWrapper extends Composite implements
			HasChangeHandlers, HasValidators {

		private VerticalPanel wrapper = new VerticalPanel();
		public RelationSelectionListWrapper(CrudServiceAsync service) {
			wrapper.add(selectionList);

			final String relatedEntityClassName = field.getRelatedEntityName();

			service.isCrudEnabled(relatedEntityClassName, new AsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						RelatedEntityEditAnchor addNew = new RelatedEntityEditAnchor(relatedEntityClassName, new EditForm.SaveCancelListener() {

							public void onSave() {
								selectionList.load();
							}

							public void onPartialSave(String warning) {
								//do nothing
							}

							public void onCancel() {
								//do nothing
							}
						});

						wrapper.add(addNew);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					throw new RuntimeException(caught);
				}
			});

			initWidget(wrapper);
			addStyleName("k5-RelationSelectionListWrapper");
		}

		public HandlerRegistration addChangeHandler(ChangeHandler handler) {
			return selectionList.addChangeHandler(handler);
		}

		public void addInputFieldValidator(InputFieldValidator validator) {
			selectionList.addInputFieldValidator(validator);
		}

		public void setValidationError(String validationError) {
			selectionList.setValidationError(validationError);
		}
		
		@Override
		public void highlight() {
			selectionList.highlight();
		}

		public boolean validate() {
			return selectionList.validate();
		}
	}

	private ManyToOneRelationField field;
	private RelationSelectionList selectionList;
	private RelationSelectionListWrapper selectionListWrapper;

	public SelectionListField(final ManyToOneRelationField field, CrudServiceAsync service) {
		this.field = field;
		this.selectionList = new RelationSelectionList(field.isRequired(), field.getRelatedEntityName());
		this.selectionList.setValue((Long) field.getValue());
		selectionListWrapper = new RelationSelectionListWrapper(service);
	}

	public CrudField getCrudField() {
		field.setValue(selectionList.getValue());
		return field;
	}

	public Widget getDisplayWidget() {
		return selectionListWrapper;
	}

	public Object getValue() {
		return selectionList.getValue();
	}

	public void load(Object value) {
		selectionList.setValue((Long) value);
	}

}

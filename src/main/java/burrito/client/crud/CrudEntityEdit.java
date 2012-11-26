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

package burrito.client.crud;

import java.util.ArrayList;
import java.util.List;

import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.generic.CrudField;
import burrito.client.crud.input.CrudInputField;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.util.CrudPreviewOpener;
import burrito.client.widgets.form.EditForm;
import burrito.client.widgets.form.EditFormMessages;
import burrito.client.widgets.inputfield.InputField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Generic panel that displays a form that can be used to edit/add an entity.
 * 
 * @author henper
 * 
 */
public class CrudEntityEdit extends EditForm {

	private CrudServiceAsync service = GWT.create(CrudService.class);
	private EditFormMessages messages = GWT.create(EditFormMessages.class);
	private List<CrudInputField<?>> fields = new ArrayList<CrudInputField<?>>();
	private CrudEntityDescription desc;
	private InputField<?> firstField;
	private Long savedId;
	private Long copyFromId;

	public CrudEntityEdit(final CrudEntityDescription desc) {
		this(desc, null);
	}
	
	public CrudEntityEdit(final CrudEntityDescription desc, Long copyFromId) {
		super();

		this.desc = desc;
		this.copyFromId = copyFromId;

		init();

		setSaveCancelListener(new SaveCancelListener() {

			public void onSave() {
				History.newItem(desc.getEntityName());
			}

			public void onPartialSave(String warning) {
				History.newItem(desc.getEntityName());
			}

			public void onCancel() {
				History.newItem(desc.getEntityName());
			}
		});

		getSaveButton().setEnabled(true);

		if (desc.isPreviewable()) {
			Button previewButton = new Button(messages.preview());

			previewButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					service.getPreviewPayload(updateCrudEntityDescription(), new AsyncCallback<CrudPreviewPayload>() {
						public void onSuccess(CrudPreviewPayload payload) {
							CrudPreviewOpener opener = new CrudPreviewOpener(payload.getPreviewUrl(), payload.getPreviewData());
							opener.open();
						}

						public void onFailure(Throwable caught) {
							displayErrorMessage(messages.previewFailed());
						}
					});
				}
			});

			addExtraButton(previewButton);
		}

		focus();
	}

	private void init() {

		for (CrudField field : desc.getFields()) {
			CrudInputField<?> inputField = CrudFieldResolver.createInputField(field, service);
			if (inputField == null) {
				continue;
			}
			String label = getDisplayLabel(field, desc.getEntityName());
			String description = getDescriptionLabel(field, desc
					.getEntityName());
			Widget w = inputField.getDisplayWidget();
			if (firstField == null && w instanceof InputField) {
				firstField = (InputField<?>) w;
			}
			String underscore = desc.getEntityName().replace('.', '_');
			w.addStyleName(underscore + "_" + field.getName());
			w.addStyleName(underscore);
			add(w, label, description);
			fields.add(inputField);
		}

	}

	private String getDescriptionLabel(CrudField field, String entityName) {
		String fullFieldName = entityName.replace('.', '_') + "_"
				+ field.getName() + "_desc";
		String fallback = field.getName() + "_desc";
		return CrudLabelHelper.getNullableString(fullFieldName, fallback);
	}

	private String getDisplayLabel(CrudField field, String entityName) {
		return getDisplayLabel(field.getName(), entityName);
	}

	private String getDisplayLabel(String fieldName, String entityName) {
		String fullFieldName = entityName.replace('.', '_') + "_" + fieldName;
		return CrudLabelHelper.getString(fullFieldName, fieldName);
	}

	@Override
	public void doLoad(String id, LoadedCallback loadedCallback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doLoadNew() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSave(final SaveCallback saveCallback) {
		service.save(updateCrudEntityDescription(), copyFromId, new AsyncCallback<Long>() {

			public void onSuccess(Long result) {
				savedId = result;
				saveCallback.success();
			}

			public void onFailure(Throwable caught) {
				String errorMessage;
				if (caught instanceof FieldValueNotUniqueException) {
					errorMessage = messages.fieldValueNotUniqueError(getDisplayLabel(((FieldValueNotUniqueException) caught).getFieldName(), desc.getEntityName()));
				}
				else if (caught instanceof CrudGenericException) {
					errorMessage = ((CrudGenericException) caught).getMessage();
				}
				else {
					errorMessage = "Failed to save entity " + desc.getEntityName();
				}
				saveCallback.failed(errorMessage);
			}
		});
	}

	/**
	 * Gets the current value
	 * 
	 * @return
	 */
	public CrudEntityDescription getValue() {
		return updateCrudEntityDescription();
	}

	private CrudEntityDescription updateCrudEntityDescription() {
		desc.setFields(parseFields());
		return desc;
	}

	private ArrayList<CrudField> parseFields() {
		ArrayList<CrudField> fields = new ArrayList<CrudField>();
		for (CrudInputField<?> crudInputField : this.fields) {
			fields.add(crudInputField.getCrudField());
		}
		return fields;
	}

	@Override
	public void doSaveNew(SaveCallback saveCallback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focus() {
		if (firstField == null) {
			return;
		}
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			
			@Override
			public void execute() {
				firstField.setFocus(true);
				firstField.selectAll();
			}
		});
	}

	public Long getSavedId() {
		return savedId;
	}

}

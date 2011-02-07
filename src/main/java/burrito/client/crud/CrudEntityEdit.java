package burrito.client.crud;

import java.util.ArrayList;
import java.util.List;

import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.generic.CrudField;
import burrito.client.crud.input.CrudInputField;
import burrito.client.crud.labels.CrudLabelHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.kanal5.play.client.widgets.form.EditForm;
import com.kanal5.play.client.widgets.inputfield.InputField;

/**
 * Generic panel that displays a form that can be used to edit/add an entity.
 * 
 * @author henper
 * 
 */
public class CrudEntityEdit extends EditForm {

	private CrudServiceAsync service = GWT.create(CrudService.class);
	private List<CrudInputField<?>> fields = new ArrayList<CrudInputField<?>>();
	private CrudEntityDescription desc;
	private InputField<?> firstField;
	private Long savedId;

	public CrudEntityEdit(final CrudEntityDescription desc) {
		super();
		this.desc = desc;

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
		String fullFieldName = entityName.replace('.', '_') + "_"
				+ field.getName();
		return CrudLabelHelper.getString(fullFieldName, field.getName());
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
		service.save(updateCrudEntityDescription(), new AsyncCallback<Long>() {

			public void onSuccess(Long result) {
				savedId = result;
				saveCallback.success();
			}

			public void onFailure(Throwable caught) {
				saveCallback.failed("Failed to save entity "
						+ desc.getEntityName());
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

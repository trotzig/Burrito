package burrito.client.crud;

import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.widgets.form.EditForm;
import burrito.client.widgets.form.EditForm.SaveCancelListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;

public class CrudEntityEditDialogBox extends DialogBox {

	private static CrudServiceAsync service = GWT.create(CrudService.class);

	private static CrudMessages labels = GWT.create(CrudMessages.class);

	private final SaveCancelListener listener;
	
	private CrudEntityEditDialogBox(String label, EditForm.SaveCancelListener listener) {
		super(false);
		this.listener = listener;
		this.addStyleName("k5-CrudEntityEditDialogBox");
		this.setText(label);
		setWidget(new Label(labels.loading()));
	}
	
	public CrudEntityEditDialogBox(String label, String entityClassName, Long id, EditForm.SaveCancelListener listener) {
		this(label, listener);
		service.describe(entityClassName, id, null,
				new AsyncCallback<CrudEntityDescription>() {

					public void onSuccess(CrudEntityDescription result) {
						init(result);
					}

					public void onFailure(Throwable caught) {
						throw new RuntimeException(
								"Failed to describe entity", caught);
					}
				});
	}

	protected void init(CrudEntityDescription result) {
		CrudEntityEdit crud = new CrudEntityEdit(result);
		crud.setSaveCancelListener(new EditForm.SaveCancelListener() {

					public void onSave() {
						removeFromParent();
						if (listener != null) {
							listener.onSave();
						}
					}

					public void onPartialSave(
							String warning) {
						throw new UnsupportedOperationException();
					}

					public void onCancel() {
						removeFromParent();
						if (listener != null) {
							listener.onCancel();
						}
					}
				});
		setWidget(crud);
		center();
	}

	public CrudEntityEditDialogBox(String label, CrudEntityDescription result,
			SaveCancelListener listener) {
		this(label, listener);
		init(result);
	}
	
}

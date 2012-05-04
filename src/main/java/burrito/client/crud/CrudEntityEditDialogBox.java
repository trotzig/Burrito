package burrito.client.crud;

import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.widgets.form.EditForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;

public class CrudEntityEditDialogBox extends DialogBox {

	private static CrudServiceAsync service = GWT.create(CrudService.class);

	private static CrudMessages labels = GWT.create(CrudMessages.class);
	
	public CrudEntityEditDialogBox(String label, String entityClassName, Long id, final EditForm.SaveCancelListener listener) {
		super(false);
		this.addStyleName("k5-CrudEntityEditDialogBox");
		this.setText(label);
		service.describe(entityClassName, id, null,
				new AsyncCallback<CrudEntityDescription>() {

					public void onSuccess(CrudEntityDescription result) {
						CrudEntityEdit crud = new CrudEntityEdit(result);
						crud
								.setSaveCancelListener(new EditForm.SaveCancelListener() {

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

					public void onFailure(Throwable caught) {
						throw new RuntimeException(
								"Failed to describe entity", caught);
					}
				});
		setWidget(new Label(labels.loading()));
	}
	
}

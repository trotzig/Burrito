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


import burrito.client.crud.CrudEntityEdit;
import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import burrito.client.widgets.InfoMessagePopup;
import burrito.client.widgets.form.EditForm;

public class RelatedEntityEditAnchor extends Anchor {

	private static CrudServiceAsync service = GWT.create(CrudService.class);
	private static CrudMessages labels = GWT.create(CrudMessages.class);
	private InfoMessagePopup popup = new InfoMessagePopup();

	public RelatedEntityEditAnchor(final String relatedEntityClassName, final EditForm.SaveCancelListener listener) {
		final String newLabel = CrudLabelHelper
				.getString(relatedEntityClassName.replace('.', '_') + "_new");
		setText(newLabel);
		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				final DialogBox box = new DialogBox(false);
				box.addStyleName("k5-RelationSelectionListWrapper-entity-edit");
				box.setText(newLabel);
				service.describe(relatedEntityClassName, -1L, null,
						new AsyncCallback<CrudEntityDescription>() {

							public void onSuccess(CrudEntityDescription result) {
								CrudEntityEdit crud = new CrudEntityEdit(result);
								crud
										.setSaveCancelListener(new EditForm.SaveCancelListener() {

											public void onSave() {
												box.removeFromParent();
												popup
														.setTextAndShow(labels
																.entityAddedAndCanBeSelected());
												listener.onSave();
											}

											public void onPartialSave(
													String warning) {
												throw new UnsupportedOperationException();
											}

											public void onCancel() {
												box.removeFromParent();
												listener.onCancel();
											}
										});
								box.setWidget(crud);
								box.center();
							}

							public void onFailure(Throwable caught) {
								throw new RuntimeException(
										"Failed to describe entity", caught);
							}
						});
				box.setWidget(new Label(labels.loading()));
				box.center();
				box.show();
			}
		});
	}
}

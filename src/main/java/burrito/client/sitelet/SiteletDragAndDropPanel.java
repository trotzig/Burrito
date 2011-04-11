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

package burrito.client.sitelet;

import java.util.ArrayList;
import java.util.List;

import burrito.client.crud.labels.CrudMessages;
import burrito.client.dto.SiteletDescription;
import burrito.client.widgets.InfoMessagePopup;
import burrito.client.widgets.draganddrop.DragAndDropPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SiteletDragAndDropPanel extends Composite {

	private static CrudMessages labels = GWT.create(CrudMessages.class);

	private VerticalPanel wrapper = new VerticalPanel();
	private SimplePanel siteletsContainer = new SimplePanel();
	private DragAndDropPanel<SiteletDescription> sortingPanel;
	private SiteletServiceAsync service = GWT.create(SiteletService.class);
	private List<Long> currentOrder;
	private List<SiteletDescription> currentModel;
	private Button saveOrderButton;
	private HorizontalPanel orderChangedPanel = new HorizontalPanel();
	SiteletDraggableWidgetCreator widgetCreator = new SiteletDraggableWidgetCreator(new AsyncCallback<SiteletDescription>() {
		
		@Override
		public void onSuccess(SiteletDescription result) {
			currentModel.remove(result);
			InfoMessagePopup popup = new InfoMessagePopup();
			popup.setTextAndShow(labels.siteletsRemoved(1));
			render();
		}
		
		@Override
		public void onFailure(Throwable caught) {
			throw new RuntimeException(caught);
		}
	});

	public SiteletDragAndDropPanel(final String containerName) {
		super();
		widgetCreator.setContainerName(containerName);
		Hyperlink addSitelet = new Hyperlink(labels.addSitelet(), "-1");

		wrapper.add(addSitelet);
		
		saveOrderButton = new Button(labels.saveOrder(), new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.saveSiteletOrder(containerName, currentOrder,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								InfoMessagePopup popup = new InfoMessagePopup();
								popup.setTextAndShow(labels.orderSaved());
								orderChangedPanel.setVisible(false);
							}

							@Override
							public void onFailure(Throwable caught) {
								throw new RuntimeException(
										"Failed to save order", caught);
							}
						});

			}
		});
		orderChangedPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		orderChangedPanel.add(new Label(labels.orderChanged()));
		orderChangedPanel.add(new HTML("&nbsp;"));
		orderChangedPanel.add(saveOrderButton);
		orderChangedPanel.setVisible(false);
		
		service.getSitelets(containerName,
				new AsyncCallback<List<SiteletDescription>>() {

					@Override
					public void onSuccess(final List<SiteletDescription> result) {
						currentModel = result;
						render();
					}

					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException("Failed to load sitelets for container "
							+ containerName, caught);
					}
				});
		wrapper.add(siteletsContainer);
		wrapper.add(orderChangedPanel);
		initWidget(wrapper);
	}

	protected void render() {
		if (currentModel == null || currentModel.isEmpty()) {
			siteletsContainer.setWidget(new Label(labels.noSiteletsHaveBeenAdded()));
		} else {
			currentOrder = convertToLongList(currentModel);
			
			sortingPanel = new DragAndDropPanel<SiteletDescription>(
					currentModel, widgetCreator) {
				@Override
				public void onOrderChanged(List<SiteletDescription> newOrder) {
					List<Long> newLongList = convertToLongList(newOrder); 
					if(!currentOrder.equals(newLongList)) {
						currentOrder = newLongList;
						orderChangedPanel.setVisible(true);
					}
				}
			};
			siteletsContainer.setWidget(sortingPanel);
		}
	}

	private List<Long> convertToLongList(List<SiteletDescription> items) {
		List<Long> longOrder = new ArrayList<Long>();
		for (SiteletDescription id : items) {
			longOrder.add(id.getEntityId());
		}
		return longOrder;
	}

}

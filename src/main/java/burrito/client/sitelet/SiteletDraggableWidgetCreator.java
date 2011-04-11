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

import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.dto.SiteletDescription;
import burrito.client.widgets.draganddrop.DraggableWidgetCreator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SiteletDraggableWidgetCreator implements DraggableWidgetCreator<SiteletDescription> {
	private static CrudMessages labels = GWT.create(CrudMessages.class);
	private SiteletServiceAsync service = GWT.create(SiteletService.class);
	private String containerName = null;
	private AsyncCallback<SiteletDescription> deleteCallback;
	
	public SiteletDraggableWidgetCreator(AsyncCallback<SiteletDescription> deleteCallback) {
		this.deleteCallback = deleteCallback;
	}
	
	@Override
	public Widget createWidget(final SiteletDescription modelObj) {
		SimplePanel wrapper = new SimplePanel();
		wrapper.addStyleName("k5-SiteLetDraggableWidget-Wrapper");

		FlowPanel inner = new FlowPanel();
		
		String headerText = CrudLabelHelper.getString(modelObj
				.getEntityName().replace('.', '_'));
		Label header = new Label(headerText);
		header.addStyleName("k5-SiteLetDraggableWidget-Header");
		inner.add(header);
		
		Label desc = new Label(modelObj.getDescription());
		desc.addStyleName("k5-SiteLetDraggableWidget-Desc");
		inner.add(desc);
		
		FlowPanel actionsPanel = new FlowPanel();
		actionsPanel.addStyleName("k5-SiteLetDraggableWidget-Actions");
		
		Hyperlink editAnchor = new Hyperlink(labels.edit(), String.valueOf(modelObj.getEntityId()));
		editAnchor.addStyleName("k5-SiteLetDraggableWidget-EditAnchor");
		editAnchor.addStyleName("k5-SiteLetDraggableWidget-Action");
		
		final Anchor deleteAnchor = new Anchor(labels.delete());
		final Label progress = new Label(labels.deleting()); 
		deleteAnchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				List<Long> idList = new ArrayList<Long>();
				idList.add(modelObj.getEntityId());
				if (!Window.confirm(labels.confirmDeleteSitelet())) {
					return;
				}
				progress.setVisible(true);
				deleteAnchor.setVisible(false);
				service.deleteSitelets(containerName, idList, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						progress.setVisible(false);
						deleteAnchor.setVisible(true);
						deleteCallback.onSuccess(modelObj);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						progress.setVisible(false);
						deleteAnchor.setVisible(true);
						deleteCallback.onFailure(caught);
						throw new RuntimeException("Failed when trying to delete sitelet with id " + modelObj.getEntityId() + " from container " + containerName, caught);
					}
				});
				event.preventDefault();
			}
		});
		progress.addStyleName("k5-SiteLetDraggableWidget-Action");
		progress.addStyleName("burrito-progress-text");
		deleteAnchor.addStyleName("k5-SiteLetDraggableWidget-DeleteAnchor");
		deleteAnchor.addStyleName("k5-SiteLetDraggableWidget-Action");
		
		actionsPanel.add(editAnchor);
		actionsPanel.add(deleteAnchor);
		progress.setVisible(false);
		actionsPanel.add(progress);
		
		inner.add(actionsPanel);
		HTML clearFloats = new HTML();
		clearFloats.addStyleName("burrito-clear-floats");
		inner.add(clearFloats);
		wrapper.setWidget(inner);
		return wrapper;
	}
	
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

}

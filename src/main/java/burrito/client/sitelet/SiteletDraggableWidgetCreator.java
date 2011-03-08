package burrito.client.sitelet;

import java.util.ArrayList;
import java.util.List;

import burrito.client.crud.CrudNameIdPair;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.widgets.draganddrop.DraggableWidgetCreator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SiteletDraggableWidgetCreator implements DraggableWidgetCreator<CrudNameIdPair> {
	private static CrudMessages labels = GWT.create(CrudMessages.class);
	private SiteletServiceAsync service = GWT.create(SiteletService.class);
	private String containerName = null;
	
	@Override
	public Widget createWidget(final CrudNameIdPair modelObj) {
		SimplePanel wrapper = new SimplePanel();
		wrapper.addStyleName("k5-SiteLetDraggableWidget-Wrapper");

		VerticalPanel inner = new VerticalPanel();
		
		String headerText = CrudLabelHelper.getString(modelObj
				.getDisplayName().replace('.', '_')) + " (id: " + modelObj.getId() + ")";
		Label header = new Label(headerText);
		header.addStyleName("k5-SiteLetDraggableWidget-Header");
		inner.add(header);
		
		FlowPanel actionsPanel = new FlowPanel();
		actionsPanel.addStyleName("k5-SiteLetDraggableWidget-Actions");
		
		Anchor editAnchor = new Anchor(labels.edit());
		editAnchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(String.valueOf(modelObj.getId()));	
				event.preventDefault();
			}
		});
		editAnchor.addStyleName("k5-SiteLetDraggableWidget-Wrapper-EditAnchor");
		
		Anchor deleteAnchor = new Anchor(labels.delete());
		deleteAnchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				List<Long> idList = new ArrayList<Long>();
				idList.add(modelObj.getId());
				if (!Window.confirm(labels.confirmDeleteSitelet())) {
					return;
				}
				service.deleteSitelets(containerName, idList, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						Window.Location.reload();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException("Failed when trying to delete sitelet with id " + modelObj.getId() + " from container " + containerName, caught);
					}
				});
				event.preventDefault();
			}
		});
		deleteAnchor.addStyleName("k5-SiteLetDraggableWidget-DeleteAnchor");
		
		actionsPanel.add(editAnchor);
		actionsPanel.add(deleteAnchor);
		
		inner.add(actionsPanel);
		
		wrapper.setWidget(inner);
		return wrapper;
	}
	
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

}

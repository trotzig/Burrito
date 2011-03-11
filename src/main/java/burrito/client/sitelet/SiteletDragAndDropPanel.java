package burrito.client.sitelet;

import java.util.ArrayList;
import java.util.List;

import burrito.client.crud.CrudNameIdPair;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.widgets.InfoMessagePopup;
import burrito.client.widgets.draganddrop.DragAndDropPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SiteletDragAndDropPanel extends Composite {

	private static CrudMessages labels = GWT.create(CrudMessages.class);

	private final String containerName;
	private VerticalPanel wrapper = new VerticalPanel();
	private DragAndDropPanel<CrudNameIdPair> sortingPanel;
	private SiteletServiceAsync service = GWT.create(SiteletService.class);
	private List<Long> currentOrder;
	private Button saveOrderButton;
	private HorizontalPanel orderChangedPanel = new HorizontalPanel();

	public SiteletDragAndDropPanel(final String siteletContainerName) {
		super();
		this.containerName = siteletContainerName;
		
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
		orderChangedPanel.add(new Label(labels.orderChanged()));
		orderChangedPanel.add(new HTML("&nbsp;"));
		orderChangedPanel.add(saveOrderButton);
		orderChangedPanel.setVisible(false);
		
		service.getSitelets(this.containerName,
				new AsyncCallback<List<CrudNameIdPair>>() {

					@Override
					public void onSuccess(final List<CrudNameIdPair> result) {
						if (result == null || result.isEmpty()) {
							wrapper.add(new Label(labels.noSiteletsHaveBeenAdded()));
						} else {
							currentOrder = convertToLongList(result);
							SiteletDraggableWidgetCreator widgetCreator = new SiteletDraggableWidgetCreator();
							widgetCreator.setContainerName(containerName);
							sortingPanel = new DragAndDropPanel<CrudNameIdPair>(
									result, widgetCreator) {
								@Override
								public void onOrderChanged(List<CrudNameIdPair> newOrder) {
									List<Long> newLongList = convertToLongList(newOrder); 
									if(!currentOrder.equals(newLongList)) {
										currentOrder = newLongList;
										orderChangedPanel.setVisible(true);
									}
								}
							};
							wrapper.add(sortingPanel);
							wrapper.add(orderChangedPanel);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException("Failed to load sitelets for container "
							+ containerName, caught);
					}
				});
		initWidget(wrapper);
	}

	private List<Long> convertToLongList(List<CrudNameIdPair> items) {
		List<Long> longOrder = new ArrayList<Long>();
		for (CrudNameIdPair id : items) {
			longOrder.add(id.getId());
		}
		return longOrder;
	}

}

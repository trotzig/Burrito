package burrito.client.sitelet;

import java.util.ArrayList;
import java.util.List;



import burrito.client.crud.CrudNameIdPair;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kanal5.play.client.widgets.InfoMessagePopup;
import com.kanal5.play.client.widgets.layout.VerticalSpacer;
import com.kanal5.play.client.widgets.panels.table.BatchAction;
import com.kanal5.play.client.widgets.panels.table.CellRenderer;
import com.kanal5.play.client.widgets.panels.table.Header;
import com.kanal5.play.client.widgets.panels.table.ItemCollection;
import com.kanal5.play.client.widgets.panels.table.PageMetaData;
import com.kanal5.play.client.widgets.panels.table.RowEditHandler;
import com.kanal5.play.client.widgets.panels.table.RowOrderHandler;
import com.kanal5.play.client.widgets.panels.table.Table;

/**
 * A panel showing a table of sitelets in a sitelet container
 * 
 * @author henper
 * 
 */
public class SiteletOverviewPanel extends Composite {
	private static CrudMessages labels = GWT.create(CrudMessages.class);

	static class SiteletsOverviewTable extends Table<CrudNameIdPair> {

		private List<CrudNameIdPair> model;

		public SiteletsOverviewTable(List<CrudNameIdPair> model) {
			super(1, true, true, true);
			this.model = model;
			addHeader(new Header(labels.siteletType()));
			addCellRenderer(new CellRenderer<CrudNameIdPair>() {

				public Widget render(CrudNameIdPair modelObj) {
					return new Label(CrudLabelHelper.getString(modelObj
							.getDisplayName().replace('.', '_')));
				}
			});

			setRowEditHandler(new RowEditHandler<CrudNameIdPair>() {

				public void onRowEditClicked(CrudNameIdPair obj) {
					History.newItem(String.valueOf(obj.getId()));
				}
			});
			render();
		}

		@Override
		protected void doLoad(PageMetaData<String> p,
				Table.LoadedCallback<CrudNameIdPair> callback) {
			// never mind the page, display all sitelets
			ItemCollection<CrudNameIdPair> collection = new ItemCollection<CrudNameIdPair>(
					model, false, 0, p.getItemsPerPage());
			callback.onReady(collection);
		}
	}

	private VerticalPanel wrapper = new VerticalPanel();
	private HorizontalPanel orderSavePanel = new HorizontalPanel();
	private SimplePanel tablePlaceHolder = new SimplePanel();
	private SiteletsOverviewTable table;
	private SiteletServiceAsync service = GWT.create(SiteletService.class);
	private List<CrudNameIdPair> currentOrder;
	private String containerName;

	public SiteletOverviewPanel(final String containerName) {
		this.containerName = containerName;
		wrapper.add(new Hyperlink(labels.addSitelet(), "-1"));
		wrapper.add(new VerticalSpacer(10));

		tablePlaceHolder.setWidget(new Label(labels.loading()));
		wrapper.add(tablePlaceHolder);

		loadTable();
		wrapper.add(new VerticalSpacer(10));
		
		orderSavePanel.setVisible(false);
		orderSavePanel.add(new Label(labels.orderChanged()));
		orderSavePanel.add(new HTML("&nbsp;"));
		orderSavePanel.add(new Button(labels.saveOrder(), new ClickHandler() {

			public void onClick(ClickEvent event) {
				saveCurrentOrder();
			}
		}));

		wrapper.add(orderSavePanel);
		initWidget(wrapper);
	}

	private void loadTable() {
		service.getSitelets(containerName,
				new AsyncCallback<List<CrudNameIdPair>>() {

					public void onSuccess(List<CrudNameIdPair> result) {
						if (result == null || result.isEmpty()) {
							tablePlaceHolder.setWidget(new Label(labels
									.noSiteletsHaveBeenAdded()));
						} else {
							table = new SiteletsOverviewTable(result);
							table
									.setRowOrderHandler(new RowOrderHandler<CrudNameIdPair>() {

										public void onRowsReordered(
												List<CrudNameIdPair> newOrder) {
											currentOrder = newOrder;
											orderSavePanel.setVisible(true);
										}

									});
							table.addBatchAction(createDeleteBatchAction());
							table.render();
							tablePlaceHolder.setWidget(table);
						}
					}

					public void onFailure(Throwable caught) {
						throw new RuntimeException(
								"Failed to load sitelets for container "
										+ containerName, caught);
					}
				});
	}

	protected BatchAction<CrudNameIdPair> createDeleteBatchAction() {
		return new BatchAction<CrudNameIdPair>(labels.delete(), labels
				.deleteDescription()) {

			@Override
			public String getSuccessText(List<CrudNameIdPair> removed) {
				return labels.siteletsRemoved(removed.size());
			}

			@Override
			public void performAction(List<CrudNameIdPair> selected,
					final AsyncCallback<Void> callback) {
				List<Long> ids = convertToLongList(selected);
				service.deleteSitelets(containerName, ids, new AsyncCallback<Void>() {
					public void onSuccess(Void result) {
						callback.onSuccess(result);
						loadTable();
					}

					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}
				});
			}
		};
	}

	private void saveCurrentOrder() {
		List<Long> longOrder = convertToLongList(currentOrder);
		service.saveSiteletOrder(containerName, longOrder, new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				InfoMessagePopup popup = new InfoMessagePopup();
				popup.setTextAndShow(labels.orderSaved());
				orderSavePanel.setVisible(false);
			}

			public void onFailure(Throwable caught) {
				throw new RuntimeException("Failed to save order", caught);
			}
		});
		
	}


	private List<Long> convertToLongList(List<CrudNameIdPair> items) {
		List<Long> longOrder = new ArrayList<Long>();
		for (CrudNameIdPair id : items) {
			longOrder.add(id.getId());
		}
		return longOrder;
	}
}

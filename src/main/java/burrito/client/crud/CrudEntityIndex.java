package burrito.client.crud;

import java.util.Date;
import java.util.List;

import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.generic.CrudEntityList;
import burrito.client.crud.generic.CrudField;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.widgets.layout.VerticalSpacer;
import burrito.client.widgets.panels.table.BatchAction;
import burrito.client.widgets.panels.table.CellRenderer;
import burrito.client.widgets.panels.table.Header;
import burrito.client.widgets.panels.table.PageMetaData;
import burrito.client.widgets.panels.table.RowEditHandler;
import burrito.client.widgets.panels.table.Table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Generic panel that displays a table of rows based on an entity. The entity
 * values are fetched from the server. The table is paginated.
 * 
 * @author henper
 * 
 */
public class CrudEntityIndex extends Composite {

	/**
	 * The table used to display the entity rows
	 * 
	 * @author henper
	 * 
	 */
	private class CrudEntityTable extends Table<CrudEntityDescription> {

		private String entityName;

		public CrudEntityTable(final String entityName, final CrudEntityDescription headers) {
			super(headers.getFields().size() + (headers.isCloneable() ? 1 : 0), true, true);
			this.entityName = entityName;
			String underscoreName = entityName.replace('.', '_');

			for (CrudField f : headers.getFields()) {
				Header h = null;
				String displayName = CrudLabelHelper.getString(underscoreName
						+ "_" + f.getName(), f.getName());
				if (f.isSortable()) {	
					h = new Header(displayName, f.getName());
				} else {
					h = new Header(displayName);
				}
				addHeader(h, f.isDefaultSort());
				if (f.isDefaultSort()) {
					setSortAscending(f.isSortAscending());
				}
			}

			if (headers.isCloneable()) {
				addHeader(new Header(""));
			}

			for (final CrudField f : headers.getFields()) {
				addCellRenderer(new CellRenderer<CrudEntityDescription>() {

					public Widget render(CrudEntityDescription modelObj) {
						return new Label(valueToString(modelObj.getValue(f
								.getName())));
					}
				});
			}
			if (headers.isCloneable()) {
				addCellRenderer(new CellRenderer<CrudEntityDescription>() {

					public Widget render(CrudEntityDescription modelObj) {
						return new Hyperlink(labels.clone(), headers.getEntityName() + "/-1/" + modelObj.getId());
					}
				});
			}
			setRowEditHandler(new RowEditHandler<CrudEntityDescription>() {

				public void onRowEditClicked(CrudEntityDescription obj) {
					History.newItem(obj.getEntityName() + "/" + obj.getId());
				}
			});
			addBatchAction(new BatchAction<CrudEntityDescription>(messages
					.delete(), messages.deleteDescription()) {

				@Override
				public void performAction(List<CrudEntityDescription> selected,
						AsyncCallback<Void> callback) {
					remove(selected, callback);
				}

				@Override
				public String getSuccessText(List<CrudEntityDescription> removed) {
					return messages.entitiesRemoved(removed.size(),
							CrudLabelHelper.getString(entityName.replace('.',
									'_')));
				}
			});
			render();
		}

		protected String valueToString(Object value) {
			if (value == null) {
				return null;
			}
			if (value instanceof Date) {
				return format.format((Date) value);
			}
			return String.valueOf(value);
		}

		protected void remove(List<CrudEntityDescription> selected,
				AsyncCallback<Void> callback) {
			service.deleteEntities(selected, callback);
		}

		@Override
		protected void doLoad(PageMetaData<String> p,
				final LoadedCallback<CrudEntityDescription> callback) {
			String filter = search.getFilter();
			service.listEntities(filter, entityName, p,
					new AsyncCallback<CrudEntityList>() {

						public void onSuccess(CrudEntityList result) {
							callback.onReady(result);
						}

						public void onFailure(Throwable caught) {
							throw new RuntimeException(
									"Failed to list entities of type "
											+ entityName, caught);
						}
					});

		}

	}

	/**
	 * The search part of the page
	 * 
	 * @author henper
	 * 
	 */
	private class SearchWidget extends Composite {
		private VerticalPanel wrapper = new VerticalPanel();
		private TextBox textBox;
		private boolean disabled = false;

		public SearchWidget() {
			initWidget(wrapper);
			addStyleName("k5-CrudEntityIndex-search");
		}

		/**
		 * Gets the current filter
		 * 
		 * @return
		 */
		public String getFilter() {
			if (disabled) {
				return null;
			}
			String query = textBox.getText();
			if (query.isEmpty()) {
				return null;
			}
			return query;
		}

		public void load() {
			textBox = new TextBox();
			textBox.addKeyUpHandler(new KeyUpHandler() {
				
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						table.reload();
					} else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
						textBox.setText(null);
						table.reload();
					}
				}
			});
			
			FlowPanel hp = new FlowPanel();
			hp.add(new HTML(labels.searchForEntity(CrudLabelHelper.getString(
					underscoreEntityName).toLowerCase())
					+ "&nbsp;"));
			hp.add(textBox);
			hp.add(new Button(labels.search(), new ClickHandler() {

				public void onClick(ClickEvent event) {
					table.reload();
				}
			}));
			Button clearButton = new Button("x");
			clearButton.setTitle(labels.clearSearchResults());
			clearButton.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					textBox.setValue(null);
					table.reload();
				}
			});
			hp.add(clearButton);
			wrapper.add(new VerticalSpacer(15));
			wrapper.add(hp);
			wrapper.add(new VerticalSpacer(5));

		}

	}

	private static DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM);
	private static CrudServiceAsync service = GWT.create(CrudService.class);
	private static CrudMessages messages = GWT.create(CrudMessages.class);
	private CrudEntityTable table;
	private VerticalPanel wrapper = new VerticalPanel();
	private Label description = new Label();
	private SimplePanel tablePlaceHolder = new SimplePanel();
	private static CrudMessages labels = GWT.create(CrudMessages.class);
	private SearchWidget search = new SearchWidget();
	private String underscoreEntityName;

	public CrudEntityIndex(final String entityName) {
		tablePlaceHolder.setWidget(new Label(labels.loading()));
		description.addStyleName("k5-CrudEntityIndex-description");
		underscoreEntityName = entityName.replace('.', '_');
		description.setText(CrudLabelHelper.getString(underscoreEntityName
				+ "_desc"));
		wrapper.add(description);
		wrapper.add(new Hyperlink(CrudLabelHelper
				.getString(underscoreEntityName + "_new"), entityName + "/-1"));
		wrapper.add(new VerticalSpacer(10));
		wrapper.add(search);

		initWidget(wrapper);
		wrapper.add(tablePlaceHolder);
		service.getEntityHeaders(entityName,
				new AsyncCallback<CrudEntityDescription>() {

					public void onSuccess(CrudEntityDescription result) {
						loadTable(entityName, result);
					}

					public void onFailure(Throwable caught) {
						throw new RuntimeException(
								"Failed to get entity description for "
										+ entityName, caught);
					}
				});
		addStyleName("k5-CrudEntityIndex");
	}

	protected void loadTable(String entityName, CrudEntityDescription result) {
		search.load();
		table = new CrudEntityTable(entityName, result);
		tablePlaceHolder.setWidget(table);
	}

}

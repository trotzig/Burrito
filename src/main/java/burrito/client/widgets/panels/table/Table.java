package burrito.client.widgets.panels.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import burrito.client.widgets.inputfield.StringInputField;
import burrito.client.widgets.panels.table.BatchAction;
import burrito.client.widgets.panels.table.CellRenderer;
import burrito.client.widgets.panels.table.Header;
import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.PageController;
import burrito.client.widgets.panels.table.PageControllerHandler;
import burrito.client.widgets.panels.table.PageMetaData;
import burrito.client.widgets.panels.table.RowEditHandler;
import burrito.client.widgets.panels.table.RowOrderHandler;
import burrito.client.widgets.panels.table.TableMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import burrito.client.widgets.InfoMessagePopup;
import burrito.client.widgets.layout.VerticalSpacer;

/**
 * Sortable, clickable and selectable table.
 * <ul>
 * <li>Sortable - headers can be clicked to sort rows</li>
 * <li>Selectable - a checkbox can be clicked to select multiple rows</li>
 * </ul>
 * 
 * @author henper
 * @param T
 *            the type of object contained within the model list
 */
@SuppressWarnings("deprecation")
public abstract class Table<T extends Serializable> extends Composite {

	public static interface LoadedCallback<T extends Serializable> {
		void onReady(ItemCollection<T> result);
	}

	private Grid table;
	private Label loadingLabel;
	private int numberOfColumns;
	private int numberOfModelColumns;
	private List<CellRenderer<T>> cellRenderers = new ArrayList<CellRenderer<T>>();
	private RowEditHandler<T> rowEditHandler;
	private RowOrderHandler<T> rowOrderHandler;
	private List<Header> tableHeader = new ArrayList<Header>();
	private boolean sortAscending = true;
	private Header currentHeader;
	private List<T> currentModel;
	private DockPanel dock = new DockPanel();
	private VerticalPanel batchJobs = new VerticalPanel();
	private TableMessages messages = GWT.create(TableMessages.class);
	private boolean rowsSelectable;
	private boolean rowsEditable;
	private boolean rowsOrderable;
	private InfoMessagePopup infoPopup = new InfoMessagePopup();
	private PageController pageController = new PageController();
	private int itemsPerPage = 20;
	private StringInputField filterText = new StringInputField(false);;
	private HorizontalPanel search = new HorizontalPanel();
	private boolean searchable;
	private CheckBox selectAll;
	
	
	/**
	 * Creates a new table. Don't forget to make calls to addHeader(),
	 * addCellRenderer() and render() after creating the object.
	 * 
	 * @param numberOfColumns
	 *            The number of columns. Only count those that you will specify
	 *            (i.e. Don't count the select checkbox as a column)
	 * @param rowsSelectable
	 *            true if rows should be selectable. This will work in
	 *            combination with addBatchAction()
	 * @param rowsEditable
	 *            true if an edit-link should be rendered next to each row
	 */
	public Table(int numberOfColumns, boolean rowsSelectable,
			boolean rowsEditable) {
		this(numberOfColumns, rowsSelectable, rowsEditable, false);
	}

	/**
	 * Creates a new table. Don't forget to make calls to addHeader(),
	 * addCellRenderer() and render() after creating the object.
	 * 
	 * @param numberOfColumns
	 *            The number of columns. Only count those that you will specify
	 *            (i.e. Don't count the select checkbox as a column)
	 * @param rowsSelectable
	 *            true if rows should be selectable. This will work in
	 *            combination with addBatchAction()
	 * @param rowsEditable
	 *            true if an edit-link should be rendered next to each row
	 * @param rowsOrderable
	 *            true if rows should be able to re-order
	 */
	public Table(int numberOfColumns, boolean rowsSelectable,
			boolean rowsEditable, boolean rowsOrderable) {
		this.rowsSelectable = rowsSelectable;
		this.rowsEditable = rowsEditable;
		this.numberOfModelColumns = numberOfColumns;
		this.numberOfColumns = numberOfModelColumns;
		this.rowsOrderable = rowsOrderable;

		if (rowsSelectable) {
			this.numberOfColumns++;
		}
		if (rowsEditable) {
			this.numberOfColumns++;
		}
		if (rowsOrderable) {
			this.numberOfColumns++;
		}
		this.table = new Grid(1, this.numberOfColumns);
		this.loadingLabel = new Label(messages.loading());
		FlowPanel centerWrapper = new FlowPanel();
		centerWrapper.add(loadingLabel);
		centerWrapper.add(table);
		
		Button searchButton= new Button(messages.searchButton());
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				load(0);
			}
		});
		Button resetButton= new Button(messages.resetButton());
		resetButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				filterText.setValue("");
				load(0);
			}
		});
		
		search.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		search.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		search.add(new Label(messages.search()));
		search.add(filterText);
		search.add(searchButton);
		search.add(resetButton);
		search.setStyleName("k5-table-searchbox");
		search.setVisible(false);
		centerWrapper.add(search);
		
		
		dock.add(pageController, DockPanel.NORTH);
		dock.add(centerWrapper, DockPanel.CENTER);
		dock.add(batchJobs, DockPanel.SOUTH);

		pageController.addPageControllerHandler(new PageControllerHandler() {

			@Override
			public void loadPage(int zeroIndexedPage) {
				load(zeroIndexedPage);
			}
		});

		initWidget(dock);
		this.setStyleName("k5-Table");
	}

	/**
	 * Adds a cell renderer. The order in which this method is called is
	 * important. The first cell renderer added will be used to render cells in
	 * the first column. The second cell renderer added will be used to render
	 * cells in the second column.
	 * 
	 * @param renderer
	 */
	public void addCellRenderer(CellRenderer<T> renderer) {
		cellRenderers.add(renderer);
	}
	
	public void selectAll( boolean all){
		for (int row = 1; row < currentModel.size() + 1; row++) {
			CheckBox cb = (CheckBox) table.getWidget(row, 0);
			
			if(all){
				cb.setValue(true);
				
				table.getRowFormatter().addStyleName(
						row, "k5-Table-row-selected");			
			}else{
				cb.setValue(false);
				
				table.getRowFormatter().removeStyleName(
						row, "k5-Table-row-selected");
			}						
		}
	}

	/**
	 * Adds a header to this table. The order in which this method is called is
	 * important. The first call will be used as header for the first column.
	 * The second call will be used as a header for the second column.
	 * 
	 * @param header
	 */
	public void addHeader(Header header) {
		addHeader(header, false);
	}

	
	/**
	 * Adds a header to this table. The order in which this method is called is
	 * important. The first call will be used as header for the first column.
	 * The second call will be used as a header for the second column.
	 * 
	 * If def is true, this header will be default sort order even if not first
	 * 
	 * @param header
	 * @param def 
	 */
	public void addHeader(Header header, boolean def) {
		tableHeader.add(header);
		if (currentHeader != null && !def) {
			// default header already set and def is not true
			return;
		}
		if (header.getKey() != null) {
			if (currentHeader!=null) {
				currentHeader.removeStyleName("current-sort");
				currentHeader.removeStyleName("ascending");
			}
			currentHeader = header;
			header.addStyleName("current-sort");
			header.addStyleName("ascending");
		}
	}

	/**
	 * Sets the handler for row edit clicks
	 * 
	 * @param rowEditHandler
	 */
	public void setRowEditHandler(RowEditHandler<T> rowEditHandler) {
		this.rowEditHandler = rowEditHandler;
	}

	/**
	 * Sets the handler for row order clicks
	 * 
	 * @param rowOrderHandler
	 */
	public void setRowOrderHandler(RowOrderHandler<T> rowOrderHandler) {
		this.rowOrderHandler = rowOrderHandler;
	}

	/**
	 * Renders the table. Make sure you have made som calls to addHeader() and
	 * addCellRenderer() before calling this method.
	 */
	public void render() {
		if (isSearchable()) {
			search.setVisible(true);
		}
		renderHeader();
		load(0);
	}

	protected void renderHeader() {
		int i = 0;
		if (rowsSelectable) {
			selectAll = new CheckBox();
			
			selectAll.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					selectAll(((CheckBox) event.getSource()).getValue());
				}
			});
			table.setWidget(0, i, selectAll);
			i = 1;
		}
		
				
		for (final Header head : tableHeader) {
			final String sortKey = head.getKey();
			if (sortKey != null) {
				head.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						if (currentHeader != null) {
							currentHeader.removeStyleName("current-sort");
							currentHeader.removeStyleName("ascending");
							currentHeader.removeStyleName("descending");
						}

						if (currentHeader != null
								&& currentHeader.getKey() == sortKey) {
							// came comparator, simply turn the list upside down
							setSortAscending(!isSortAscending());
						} else {
							currentHeader = head;
							setSortAscending(true);
						}
						currentHeader.addStyleName("current-sort");
						if (isSortAscending()) {
							currentHeader.addStyleName("ascending");
						} else {
							currentHeader.addStyleName("descending");
						}
						load(0);
					}
				});
				table.getCellFormatter()
						.addStyleName(0, i, "sortable");
			}
			table.setWidget(0, i, head);
			i++;
		}

		table.getRowFormatter().addStyleName(0, "header");
	}

	private void load(int page) {
		String sortKey = null;
		loadingLabel.setVisible(true);
		table.setVisible(false);
		batchJobs.setVisible(false);
		if (rowsSelectable) {
			selectAll.setValue(false);
		}
		if (currentHeader != null) {
			sortKey = currentHeader.getKey();
		}
		PageMetaData<String> pmd = new PageMetaData<String>(itemsPerPage, page,
				sortKey, isSortAscending());
		pmd.setFilterText(filterText.getValue());
		doLoad(pmd, new LoadedCallback<T>() {
			@Override
			public void onReady(ItemCollection<T> result) {
				currentModel = result.getItems();
				pageController.update(result.getTotalKnownPages(), result
						.getPage());
				renderTable();
				loadingLabel.setVisible(false);
				table.setVisible(true);
				batchJobs.setVisible(true);
			}
		});
	}

	/**
	 * Called when the table is about to be loaded or if a table header which is
	 * sortable has been clicked
	 * 
	 * @param page
	 * @param sortKey
	 *            May be null
	 * @param ascending
	 * @param callback
	 */
	protected abstract void doLoad(PageMetaData<String> p,
			LoadedCallback<T> callback);

	protected void renderTable() {
		// remove all previous rows 
		for (int i = 1; i < table.getRowCount(); i++) {
			table.removeRow(i);
		}
		int leftMargin = 0;
		if (rowsSelectable) {
			leftMargin++; // add margin because of select box
		}

		table.resize(currentModel.size() + 1, this.numberOfColumns);
		int row = 1;
		for (final T obj : currentModel) {
			String[] names = table.getRowFormatter().getStyleName(row).split(" ");
			for (String style: names) {
				if (style.length()>0) table.getRowFormatter().removeStyleName(row, style);
			}
			if (rowsSelectable) {
				final int thisRowAsFinal = row;
				CheckBox select = new CheckBox();
				select.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						if (event.getValue().booleanValue()) {
							table.getRowFormatter().addStyleName(
									thisRowAsFinal, "k5-Table-row-selected");
						} else {
							table.getRowFormatter().removeStyleName(
									thisRowAsFinal, "k5-Table-row-selected");
						}
					}
				});
				table.setWidget(row, 0, select);
			}
			for (int column = 0; column < numberOfModelColumns; column++) {
				table.setWidget(row, column + leftMargin, cellRenderers.get(
						column).render(obj));
			}
			table.getRowFormatter().addStyleName(row, "k5-Table-row");
			if (row % 2 == 0) {
				table.getRowFormatter().addStyleName(row, "k5-Table-row-even");
			} else {
				table.getRowFormatter().addStyleName(row, "k5-Table-row-odd");
			}
			String rowStyle = rowStyle(row, obj);
			if (rowStyle!=null) {
				table.getRowFormatter().addStyleName(row, rowStyle);
			}
			
			if (rowsOrderable && rowOrderHandler != null) {
				final int fRow = row;
				Anchor up = new Anchor(messages.up());
				up.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						move(fRow - 1, -1);
					}
				});
				up.addStyleName("k5-Table-order-up");
				Anchor down = new Anchor(messages.down());
				down.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						move(fRow - 1, 1);
					}
				});
				down.addStyleName("k5-Table-order-down");
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(up);
				hp.add(down);
				int rightMargin = 1;
				if (rowsEditable) {
					rightMargin++;
				}
				table.setWidget(row, numberOfColumns - rightMargin, hp);
			}
			if (rowsEditable && rowEditHandler != null) {
				Anchor edit = new Anchor(messages.edit());
				edit.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						rowEditHandler.onRowEditClicked(obj);
					}
				});
				table.setWidget(row, numberOfColumns - 1, edit);
			}

			row++;
		}

	}
	
	/**
	 * Style row based on row number and/or the object presented in row 
	 * 
	 * @param row
	 * @param obj
	 * @return style-string 
	 */
	protected String rowStyle(int row, T obj) {
		return null;
	}


	/**
	 * Moves a model obj in the list
	 * 
	 * @param modelIndex
	 * @param step
	 */
	protected void move(int modelIndex, int step) {
		try {
			T obj = currentModel.get(modelIndex);
			T switcher = currentModel.get(modelIndex + step);
			currentModel.set(modelIndex, switcher);
			currentModel.set(modelIndex + step, obj);
			renderTable();
			rowOrderHandler.onRowsReordered(currentModel);
		} catch (IndexOutOfBoundsException e) {
			// ignore, expected when trying to reorder last or first item.
		}
	}

	/**
	 * Gets all selected rows
	 * 
	 * @return
	 */
	public List<T> getSelectedRows() {
		List<T> result = new ArrayList<T>();
		for (int row = 1; row < currentModel.size() + 1; row++) {
			CheckBox cb = (CheckBox) table.getWidget(row, 0);
			if (cb.getValue()) {
				result.add(currentModel.get(row - 1));
			}
		}
		return result;
	}

	/**
	 * Adds a batch action to this table. A batch action is performed on
	 * selected rows in the table.
	 * 
	 * @param batchAction
	 */
	public void addBatchAction(final BatchAction<T> batchAction) {
		batchJobs.add(new VerticalSpacer(10));
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		final Button b = new Button(batchAction.getButtonText());
		ClickHandler ch = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final List<T> selected = getSelectedRows();
				if (selected == null || selected.isEmpty()) {
					return;
				}
				batchAction.performAction(selected, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						b.setEnabled(true);
						b.removeStyleName("loading");
						infoPopup.setTextAndShow(batchAction
								.getSuccessText(selected));
						load(pageController.getCurrentPage());
					}

					@Override
					public void onFailure(Throwable caught) {
						b.setEnabled(true);
						b.removeStyleName("loading");
						throw new RuntimeException(caught);
					}
				});
				b.setEnabled(false);
				b.addStyleName("loading");
			}
		};
		b.addClickHandler(ch);
		hp.add(b);
		hp.add(new Label(batchAction.getDescription()));
		batchJobs.add(hp);
	}

	/**
	 * Gets all currently visible rows
	 */
	public List<T> getAllRows() {
		return currentModel;
	}

	/**
	 * Reloads the table from the page 0 and current sorting
	 */
	public void reload() {
		load(0);
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}

	public boolean isSortAscending() {
		return sortAscending;
	}

	/**
	 * Sets the total amount of items per page.
	 * 
	 * @param itemsPerPage
	 */
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	
	
}

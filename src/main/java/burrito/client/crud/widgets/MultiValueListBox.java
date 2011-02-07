package burrito.client.crud.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



import burrito.client.crud.CrudNameIdPair;
import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public class MultiValueListBox extends ListBox {

	public static Comparator<CrudNameIdPair> DISPLAY_NAME_SORTER = new Comparator<CrudNameIdPair>() {

		public int compare(CrudNameIdPair o1, CrudNameIdPair o2) {
			return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
		}
	};
	private CrudServiceAsync service = GWT.create(CrudService.class);
	private List<Long> waitingToBeSelected;
	private boolean initialized = false;
	private String entityName;

	public MultiValueListBox(final String entityName) {
		super(true);
		this.entityName = entityName;
		load();
		addStyleName("k5-MultiValueListBox");

	}

	public void load() {
		final List<Long> oldValue = getSelectedValues();
		service.getListValues(entityName,
				new AsyncCallback<List<CrudNameIdPair>>() {

					public void onSuccess(List<CrudNameIdPair> result) {
						init(result);
						if (oldValue != null && !oldValue.isEmpty()) {
							setSelectedValues(oldValue);
						}
					}

					public void onFailure(Throwable caught) {
						throw new RuntimeException(
								"Failed to get list values of type "
										+ entityName);
					}
				});
	}

	protected void init(List<CrudNameIdPair> result) {
		clear();
		Collections.sort(result, DISPLAY_NAME_SORTER);
		for (CrudNameIdPair cnip : result) {
			addItem(cnip.getDisplayName(), String.valueOf(cnip.getId()));
		}
		initialized = true;
		if (waitingToBeSelected != null) {
			setSelectedValues(waitingToBeSelected);
			waitingToBeSelected = null;
		}
	}

	/**
	 * Hightlights the items in the list from the list of ids specified.
	 * 
	 * @param selected
	 */
	public void setSelectedValues(List<Long> selected) {
		if (!initialized) {
			waitingToBeSelected = selected;
			return;
		}
		for (int i = 0; i < getItemCount(); i++) {
			setItemSelected(i, selected.contains(Long.valueOf(getValue(i))));
		}
	}
	
	public List<Long> getSelectedValues() {
		List<Long> result = new ArrayList<Long>();
		for (int i = 0; i < getItemCount(); i++) {
			if (isItemSelected(i)) {
				result.add(Long.valueOf(getValue(i)));
			}
		}
		return result;
	}

}

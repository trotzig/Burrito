package burrito.client.widgets.panels.table;

public interface RowEditHandler<T> {

	/**
	 * Fired when a row has been clicked
	 * 
	 * @param obj
	 */
	void onRowEditClicked(T obj);

}

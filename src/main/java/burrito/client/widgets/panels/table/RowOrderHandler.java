package burrito.client.widgets.panels.table;

import java.util.List;

public interface RowOrderHandler<T> {

	/**
	 * Called whenever rows have been reordered
	 * 
	 * @param newOrder
	 */
	void onRowsReordered(List<T> newOrder);

}

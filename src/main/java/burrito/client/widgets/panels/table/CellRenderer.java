package burrito.client.widgets.panels.table;

import com.google.gwt.user.client.ui.Widget;

public interface CellRenderer<T> {
	/**
	 * Get a widget to be rendered from a model obj
	 * 
	 * @param modelObj
	 * @param zeroIndexedColumn
	 * @return
	 */
	Widget render(T modelObj);
}

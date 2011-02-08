package burrito.client.widgets.selection;

public abstract class SelectionListLabelCreator<T> {

	/**
	 * Creates a label to display in the selection list from an object of type T
	 * 
	 * @param obj
	 * @return
	 */
	public abstract String createLabel(T obj);

	/**
	 * Gets a special style name for this object. Default implementation returns
	 * null. Override to create custom styled selection lists.
	 * 
	 * @param obj
	 * @return
	 */
	public String getStyleName(T obj) {
		return null;
	}

}

package burrito.client.widgets.panels.table;

import java.util.List;

import burrito.client.widgets.panels.table.Table;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Object representing a batch job done on selected rows in a {@link Table}
 * 
 * @author henper
 * 
 * @param <T>
 */
public abstract class BatchAction<T> {

	private String buttonText;
	private String descritpion;

	public BatchAction(String buttonText, String descritpion) {
		this.buttonText = buttonText;
		this.descritpion = descritpion;
	}

	public String getButtonText() {
		return buttonText;
	}

	public String getDescription() {
		return descritpion;
	}

	/**
	 * The text to display as a success message
	 * 
	 * @param removed the removed objects
	 * @return
	 */
	public abstract String getSuccessText(List<T> removed);

	/**
	 * Perform the batch action
	 * 
	 * @param selected
	 *            the selected rows in the table
	 */
	public abstract void performAction(List<T> selected,
			AsyncCallback<Void> callback);

}

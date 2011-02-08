package burrito.client.widgets.panels.table;

import burrito.client.widgets.panels.table.Header;

import com.google.gwt.user.client.ui.HTML;

public class Header extends HTML {

	String key;

	/**
	 * Constructs a header which will not be sortable
	 * 
	 * @param label
	 */
	public Header(String label) {
		this(label, null);
	}

	/**
	 * Constructs a {@link Header} with a label and an associated comparator
	 * 
	 * @param label
	 * @param comparator
	 */
	public Header(String label, String key) {
		super(label);
		this.key = key;
	}

	/**
	 * Gets the sort key associated with this header
	 * 
	 * @return
	 */
	public String getKey() {
		return key;
	}

}

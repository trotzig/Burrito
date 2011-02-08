package burrito.client.widgets.panels.table;

import java.io.Serializable;

/**
 * Meta data about a page in a result set.
 * 
 * @author henper
 * @param S
 *            the sort key type
 */
public class PageMetaData<S> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int page;
	private int itemsPerPage;
	private S sortKey;
	private boolean ascending;

	private String filterText;

	public PageMetaData(int itemsPerPage, int page, S sortKey, boolean ascending) {
		this.itemsPerPage = itemsPerPage;
		this.page = page;
		this.sortKey = sortKey;
		this.ascending = ascending;
	}

	public PageMetaData() {
		// default constructor
	}

	/**
	 * Gets the current page. The returned value is zero indexed. The first page
	 * will have number 0.
	 * 
	 * @return
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Gets the number of items to show per page
	 * 
	 * @return
	 */
	public int getItemsPerPage() {
		return itemsPerPage;
	}

	/**
	 * Gets the sort key
	 * 
	 * @return
	 */
	public S getSortKey() {
		return sortKey;
	}

	/**
	 * Returns true if ascending sorting should be used
	 * 
	 * @return
	 */
	public boolean isAscending() {
		return ascending;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public void setSortKey(S sortKey) {
		this.sortKey = sortKey;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	/**
	 * Helper for persistence query
	 * 
	 * @return
	 */
	public long getRangeEnd() {
		return getRangeStart() + itemsPerPage;
	}

	/**
	 * Helper for persistence query
	 * 
	 * @return
	 */
	public long getRangeStart() {
		return page * itemsPerPage;
	}

	/**
	 * Gets the sort key with either "asc" or "desc" appended to the end of the
	 * string
	 * 
	 * @return
	 */
	public String getSortKeyWithAscending() {
		if (ascending) {
			return sortKey + " asc";
		}
		return sortKey + " desc";
	}

	public void setFilterText(String filterText) {
		this.filterText = filterText;
	}

	public String getFilterText() {
		return filterText;
	}

}

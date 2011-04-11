/**
 * Copyright 2011 Henric Persson (henric.persson@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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

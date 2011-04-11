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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import burrito.client.widgets.panels.table.ItemCollection;

/**
 * An {@link ItemCollection} contains a list of items, the current page number
 * and the total number of items
 * 
 * @author henper
 * 
 * @param <T>
 */
public class ItemCollection<T extends Serializable> implements Serializable, Iterable<T> {

	private static final long serialVersionUID = 1L;

	private List<T> items;
	private boolean hasNextPage;
	private Integer totalCount;
	private int page;
	private int itemsPerPage;

	public ItemCollection() {
		// default constructor
	}

	/**
	 * Constructor with all required parameters
	 * @param items
	 * @param hasNextPage
	 * @param page
	 * @param itemsPerPage
	 */
	public ItemCollection(List<T> items, boolean hasNextPage, int page,
			int itemsPerPage) {
		this.items = items;
		this.hasNextPage = hasNextPage;
		this.page = page;
		this.itemsPerPage = itemsPerPage;
	}

	public ItemCollection(List<T> items, int totalItemCount, int page,
			int itemsPerPage) {
		this.items = items;
		this.page = page;
		this.itemsPerPage = itemsPerPage;
		this.totalCount = totalItemCount;
		if (totalCount > ((page +1) * itemsPerPage)) {
			// more pages
			hasNextPage = true;
		} 
	}

	/**
	 * Gets the items in this collection
	 * 
	 * @return
	 */
	public List<T> getItems() {
		return items;
	}

	/**
	 * Sets the items in this collection
	 * 
	 * @param items
	 */
	public void setItems(List<T> items) {
		this.items = items;
	}

	/**
	 * Gets the total count of items contained in this collection. May return
	 * <code>null</code> if the total size is unknown
	 * 
	 * @return
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * Gets the current page. This value is zero-indexed.
	 * 
	 * @return
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Sets the current page. This value is zero-indexed.
	 * 
	 * @param page
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * Gets the number of items displayed per page
	 * 
	 * @return
	 */
	public int getItemsPerPage() {
		return itemsPerPage;
	}

	/**
	 * Sets the number of items displayed per page
	 * 
	 * @param itemsPerPage
	 */
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	/**
	 * Returns true if there is at least one more page to get
	 * 
	 * @return
	 */
	public boolean isHasNextPage() {
		return hasNextPage;
	}

	/**
	 * Set to true if there is at least one more page to get
	 * 
	 * @param hasNextPage
	 */
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	/**
	 * Sets the total number of items revealed in this collection
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * Gets the total amount of known pages in this item collection. If the
	 * total item count is known, the exact number of pages are returned. If
	 * only information about the next page is available, the current page index
	 * is used in combination with information about next page
	 * 
	 * @return
	 */
	public int getTotalKnownPages() {
		if (totalCount != null) {
			int pages = totalCount / itemsPerPage;
			int evenUp = totalCount % itemsPerPage;
			if (evenUp != 0) {
				pages++;
			}
			return pages;
		}
		if (hasNextPage) {
			return page + 2;
		}
		return page + 1;
	}

	/**
	 * Narrow down the collection to the specified number of items. Can only be
	 * done on the first page (getPage() returns 0).
	 * 
	 * @param itemsPerPage
	 * @throws IllegalStateException
	 *             If page is other than 0
	 */
	public void narrow(int itemsPerPage) {
		if (page != 0) {
			throw new IllegalStateException(
					"Item collection can only be narrowed when on first page. Current page is "
							+ page);
		}
		setItemsPerPage(itemsPerPage);
		if (items != null && items.size() > itemsPerPage) {
			List<T> newItems = new ArrayList<T>(itemsPerPage);
			for (T t : items) {
				newItems.add(t);
				if (newItems.size() == itemsPerPage) {
					break;
				}
			}
			items = newItems;
		}
	}

	/**
	 * Checks whether the current page is the last page in the collection.
	 * 
	 * @return
	 */
	public boolean isLastPage() {
		if (hasNextPage) {
			return false;
		}
		if (totalCount == null) {
			return true;
		}
		if (totalCount <= (page + 1) * itemsPerPage) {
			return true;
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

}

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

import burrito.client.widgets.panels.table.Header;

import com.google.gwt.user.client.ui.HTML;

public class Header extends HTML {

	String key;
	boolean sortAscendingOnFirstClick = true;

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
	
	public void setSortAscendingOnFirstClick(boolean sortAscendingOnFirstClick) {
		this.sortAscendingOnFirstClick = sortAscendingOnFirstClick;
	}
	
	public boolean isSortAscendingOnFirstClick() {
		return sortAscendingOnFirstClick;
	}

}

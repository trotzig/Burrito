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

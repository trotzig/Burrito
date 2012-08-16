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

package burrito.client.crud.widgets;

import java.util.ArrayList;
import java.util.List;

import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.widgets.validation.HasValidators;
import burrito.client.widgets.validation.InputFieldValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EnumListWidget extends Composite implements HasValidators {
	private VerticalPanel wrapper = new VerticalPanel();
	private CrudMessages labels = GWT.create(CrudMessages.class);
	private boolean required;
	private Label validationError = new Label(labels.atLeastOne());
	private ListBox lb = new ListBox(true);
	private List<String> pendingValue;
	private CrudServiceAsync service = GWT.create(CrudService.class);
	private boolean enumsLoaded = false;
	private final String enumClassName;
	
	

	/**
	 * Create a new linked entity widget.
	 * 
	 * @param required
	 *            true if at least one link must be specified
	 * @param allowMultiple
	 *            true if multiple links can be entered. In this case, use
	 *            getValues() to fetch a list of links. IF false, use getValue()
	 *            to get the only link.
	 */
	public EnumListWidget(boolean required, String enumClassName) {
		this.required = required;
		this.enumClassName = enumClassName;
		validationError.addStyleName("validationError");
		validationError.setVisible(false);
		wrapper.add(lb);
		wrapper.add(validationError);
		initWidget(wrapper);
		addStyleName("k5-EnumListWidget");
		loadEnumValues();
	}

	private void loadEnumValues() {
		service.getEnumListValues(enumClassName, new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> enumValues) {
				renderListBox(enumValues);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}
		});
	}

	protected void renderListBox(List<String> enumValues) {
		String enumClassNameWithUnderscore = enumClassName.replace(".", "_");
		for (String enumValue : enumValues) {
			lb.addItem(CrudLabelHelper.getString(enumClassNameWithUnderscore + "_" + enumValue), enumValue);
		}
		enumsLoaded = true;
		if (pendingValue != null) {
			renderSelectedValues(pendingValue);
			pendingValue = null;
		}
	}

	private void renderSelectedValues(List<String> values) {
		for (int i = 0; i < lb.getItemCount(); i++) {
			String val = lb.getValue(i);
			lb.setItemSelected(i, values.contains(val));
		}
	}

	public void addInputFieldValidator(InputFieldValidator validator) {
		throw new UnsupportedOperationException();
	}

	public void setValidationError(String validationError) {
		throw new UnsupportedOperationException();
	}

	public boolean validate() {
		validationError.setVisible(false);
		if (required && getValue() == null) {
			validationError.setVisible(true);
			return false;
		}
		return true;
	}

	/**
	 * Gets the list of selected items
	 * 
	 * @return
	 */
	public List<String> getValue() {
		if (pendingValue != null) {
			return pendingValue;
		}
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < lb.getItemCount(); i++) {
			if (lb.isItemSelected(i)) {
				values.add(lb.getValue(i));
			}
		}
		return values;
	}

	/**
	 * Sets the list of selected items
	 * 
	 * @param json
	 */
	public void setValue(List<String> value) {
		if (enumsLoaded) {
			renderSelectedValues(value);
		} else {
			//Set pending and wait for enum load
			pendingValue = value;
		}
		
	}
	

	@Override
	public void highlight() {
		lb.setFocus(true);
	}
	
}

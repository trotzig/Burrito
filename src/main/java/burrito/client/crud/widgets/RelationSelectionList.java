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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import burrito.client.crud.CrudNameIdPair;
import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Lists all values from an entity in a selection list. Values are fetched
 * asynchronously.
 * 
 * @author henper
 * 
 */
public class RelationSelectionList extends SelectionList<Long> {

	private String typeClassName;
	private Map<Long, String> displayNames = new HashMap<Long, String>();
	private static CrudServiceAsync service = GWT.create(CrudService.class);

	public RelationSelectionList(boolean required, String typeClassName) {
		super(required);
		this.typeClassName = typeClassName;
		setNullSelectLabel("");
		setLabelCreator(new SelectionListLabelCreator<Long>() {

			@Override
			public String createLabel(Long obj) {
				return displayNames.get(obj);
			}
		});
		load();
	}

	public void load() {
		final Long oldValue = getValue();
		service.getListValues(typeClassName,
				new AsyncCallback<List<CrudNameIdPair>>() {

					public void onSuccess(List<CrudNameIdPair> result) {
						Collections.sort(result,
								MultiValueListBox.DISPLAY_NAME_SORTER);
						List<Long> ids = new ArrayList<Long>();
						for (CrudNameIdPair crudNameIdPair : result) {
							displayNames.put(crudNameIdPair.getId(),
									crudNameIdPair.getDisplayName());
							ids.add(crudNameIdPair.getId());
						}
						setModel(ids);
						render();
						if (oldValue != null) {
							setValue(oldValue);
						}
					}

					public void onFailure(Throwable caught) {
						throw new RuntimeException("Failed to fetch list of "
								+ typeClassName, caught);
					}
				});
	}
	
}

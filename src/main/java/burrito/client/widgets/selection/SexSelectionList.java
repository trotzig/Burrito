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

package burrito.client.widgets.selection;

import java.util.ArrayList;
import java.util.List;

import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;
import burrito.client.widgets.selection.Sex;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

/**
 * Selection list for sexes
 * 
 * @author henper
 * 
 */
public class SexSelectionList extends SelectionList<Sex> {

	private CommonMessages messages = GWT.create(CommonMessages.class);

	public SexSelectionList(boolean required) {
		super(required);
		List<Sex> sexes = new ArrayList<Sex>();
		for (Sex sex : Sex.values()) {
			sexes.add(sex);
		}
		SelectionListLabelCreator<Sex> lc = new SelectionListLabelCreator<Sex>() {

			@Override
			public String createLabel(Sex obj) {
				if (obj == Sex.FEMALE) {
					return messages.sexFemale();
				} else {
					return messages.sexMale();
				}
			}
		};
		setModel(sexes);
		setLabelCreator(lc);
		setNullSelectLabel(messages.selectSex());
		render();
	}
}

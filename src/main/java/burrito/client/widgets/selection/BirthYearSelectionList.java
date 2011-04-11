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

import java.util.Date;

import burrito.client.widgets.selection.IntegerIntervalSelectionList;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

/**
 * {@link IntegerIntervalSelectionList} which displays years from 1900 up to current year.
 * Selection is preset to MAX_YEAR - 20.
 * 
 * @author joasod
 *
 */
@SuppressWarnings("deprecation")
public class BirthYearSelectionList extends IntegerIntervalSelectionList{

	private static int MIN_YEAR = 1900;
	private static int MAX_YEAR = MIN_YEAR + new Date().getYear(); //Required by GWT
	private CommonMessages messages = GWT.create(CommonMessages.class);
	
	public BirthYearSelectionList(boolean required) {
		super(required);
		setMin(MIN_YEAR);
		setMax(MAX_YEAR);
		setNullSelectLabel(messages.selectBirthYear());
		setReverse(true);
		render();
	}
}

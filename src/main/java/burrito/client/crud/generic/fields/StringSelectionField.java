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

package burrito.client.crud.generic.fields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import burrito.client.crud.generic.CrudField;

@SuppressWarnings("serial")
public class StringSelectionField extends CrudField {

	private Map<String, String> nameDisplay;
	private String value;

	public StringSelectionField(String value, String[] list) {
		nameDisplay = new HashMap<String, String>();
		for (int i = 0; i < list.length; i++) {
			String v = list[i];
			String d = list[++i];
			nameDisplay.put(v, d);
		}
		String display = nameDisplay.get(value);
		if (display != null) {
			// ensure that old value is still valid by checking the list of
			// possible values
			this.value = value;
		}
	}

	public StringSelectionField() {
		// default constructor
	}

	@Override
	public Class<?> getType() {
		return String.class;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = (String) value;
	}

	public String getLabel(String obj) {
		return nameDisplay.get(obj);
	}

	public List<String> getValues() {
		List<String> result = new ArrayList<String>();
		for (String string : nameDisplay.keySet()) {
			result.add(string);
		}
		return result;
	}

}

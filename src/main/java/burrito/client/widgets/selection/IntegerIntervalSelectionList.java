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
import java.util.Collections;
import java.util.List;

import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;

public class IntegerIntervalSelectionList extends SelectionList<Integer> {

	private int min;
	private int max;
	private boolean reverse;
	
	public IntegerIntervalSelectionList(boolean required){
		super(required);
	}
	
	public IntegerIntervalSelectionList(int min, int max, boolean required) {
		super(required);
		this.min = min;
		this.max = max;
		render();
	}
	
	public IntegerIntervalSelectionList(int min, int max, String nullSelectLabel, boolean required){
		super(required);
		this.min = min;
		this.max = max;
		setNullSelectLabel(nullSelectLabel);
		render();
	}
	
	public void render() {
		List<Integer> integers = new ArrayList<Integer>();
		for (int i = min; i <= max; i++) {
			integers.add(Integer.valueOf(i));
		}
		if(reverse){
			Collections.reverse(integers);
		}
		setModel(integers);
		setLabelCreator(new SelectionListLabelCreator<Integer>() {
			
			@Override
			public String createLabel(Integer obj) {
				return String.valueOf(obj);
			}
		});
		super.render();
	}
	

	public void setMin(int min){
		this.min = min;
	}
	
	public void setMax(int max){
		this.max = max;
	}
	
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	
}

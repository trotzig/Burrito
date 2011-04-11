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



import burrito.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StringListWidget extends Composite {

	private FlowPanel tagsPanel = new FlowPanel();
	private TextBox tagInput = new TextBox();
	private Button addButton = new Button(labels.add());
	private VerticalPanel wrapper = new VerticalPanel();
	private static CrudMessages labels = GWT.create(CrudMessages.class);

	public StringListWidget(List<String> strings) {
		tagsPanel.setStyleName("k5-StringListWidget-TagPanel");
		wrapper.add(tagsPanel);
		HorizontalPanel inputWrapper = new HorizontalPanel();
		inputWrapper.add(tagInput);
		inputWrapper.add(addButton);
		wrapper.add(inputWrapper);
		addButton.addStyleName("k5-StringListWidget-AddButton");
		addButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				addCurrentTag();
			}
		});
		
		load(strings);
		initWidget(wrapper);
	}

	private void load(List<String> values) {
		if (values == null) {
			return;
		}
		for(String value : values) {
			addTag(value);
		}
	}

	private void addTag(String tagText) {
		final Button tag = new Button(tagText.trim());
		tag.setTitle(labels.clickToDeleteTag());
		tag.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				deleteTag(tag);
			}
		});
		tag.setStyleName("k5-StringListWidget-TagPanel-TagButton");
		tagsPanel.add(tag);
	}
	
	protected void addCurrentTag() {
		addTag(tagInput.getText());
	}

	private void deleteTag(Button tag) {
		tagsPanel.remove(tag);
	}

	public List<String> getValue() {
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < tagsPanel.getWidgetCount(); i++) {
			Button b = (Button) tagsPanel.getWidget(i);
			values.add(b.getText());
		}
		return values;
	}

	public void setValue(List<String> values) {
		if (values == null) {
			return;
		}
		for(String tag : values) {
			if(tag != null && !tag.isEmpty()) {
				addTag(tag);
			}
		}

	}

}

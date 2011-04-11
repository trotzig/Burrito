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

package burrito.client.sitelet;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import burrito.client.widgets.layout.VerticalSpacer;

public class SiteletAdminPanel extends Composite implements ValueChangeHandler<String> {

	private VerticalPanel wrapper = new VerticalPanel();
	private SimplePanel contentPlaceHolder = new SimplePanel();
	private String containerName;

	public SiteletAdminPanel(String containerName) {
		this.containerName = containerName;
		wrapper.add(new VerticalSpacer(20));
		wrapper.add(contentPlaceHolder);
		contentPlaceHolder.addStyleName("k5-SiteletAdminPanel-content");
		initWidget(wrapper);
		addStyleName("k5-SiteletAdminPanel");
		setWidth("100%");
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		String historyToken = event.getValue();
		contentPlaceHolder.setWidget(createContentWidget(historyToken));
	}

	private Widget createContentWidget(String historyToken) {
		if ("".equals(historyToken)) {
			return new SiteletDragAndDropPanel(containerName);
		} 
		try {
			Long id = Long.parseLong(historyToken);
			return new SiteletEditPanel(containerName, id);
		} catch(NumberFormatException e) {
			throw new RuntimeException("Unknown history token: " + historyToken);
		}
		
	}

}

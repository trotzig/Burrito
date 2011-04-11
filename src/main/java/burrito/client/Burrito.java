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

package burrito.client;


import burrito.client.crud.CrudPanel;
import burrito.client.sitelet.SiteletAdminPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Burrito implements EntryPoint {
	
	public void onModuleLoad() {
		RootPanel adminPanel = RootPanel.get("burrito-admin");

		if (adminPanel != null) {
			String siteletContainerId = Window.Location.getParameter("container");
			if (siteletContainerId != null && !siteletContainerId.isEmpty()) {
				SiteletAdminPanel siteletAdminPanel = new SiteletAdminPanel(siteletContainerId);
				adminPanel.add(siteletAdminPanel);
				return;
			} else {
				CrudPanel crud = new CrudPanel();
				adminPanel.add(crud);
				return;
			}
		}
	}
}

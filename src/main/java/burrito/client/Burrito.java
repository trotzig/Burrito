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
import burrito.client.widgets.form.EditForm;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Burrito implements EntryPoint {
	
	private static EditForm currentEditForm;
	private static CtrlSaveHandler currentCtrlSaveHandler;
	
	public void onModuleLoad() {
		RootPanel adminPanel = RootPanel.get("burrito-admin");

		if (adminPanel != null) {
			String siteletContainerId = Window.Location.getParameter("container");
			if (siteletContainerId != null && !siteletContainerId.isEmpty()) {
				SiteletAdminPanel siteletAdminPanel = new SiteletAdminPanel(siteletContainerId);
				adminPanel.add(siteletAdminPanel);
			} else {
				CrudPanel crud = new CrudPanel();
				adminPanel.add(crud);
			}
			Window.addWindowScrollHandler(new ScrollHandler() {
				
				@Override
				public void onWindowScroll(ScrollEvent event) {
					updateEditFormButtons();
				}
			});
			Window.addResizeHandler(new ResizeHandler() {
				
				@Override
				public void onResize(ResizeEvent event) {
					updateEditFormButtons();
				}
			});
			
			Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
				
				@Override
				public void onPreviewNativeEvent(NativePreviewEvent event) {
					if (currentCtrlSaveHandler == null) {
						return;
					}
					if (event.getTypeInt() == Event.ONKEYDOWN) {
						int sCharacterCode = 83;
						if (event.getNativeEvent().getCtrlKey() && event.getNativeEvent().getKeyCode() == sCharacterCode) {
							currentCtrlSaveHandler.onCtrlSave();
							event.cancel();
						}
					}
				}
			});
		}		
	}
	
	public static void setCurrentCtrlSaveHandler(CtrlSaveHandler handler) {
		currentCtrlSaveHandler = handler;
	}
	
	public static void setCurrentEditForm(EditForm current) {
		currentEditForm = current;
		setCurrentCtrlSaveHandler(current);
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			
			@Override
			public void execute() {
				updateEditFormButtons();
			}
		});
	}

	private static void updateEditFormButtons() {
		if (currentEditForm != null) {
			int bottomOfWindow = Window.getClientHeight() + Window.getScrollTop();
			int bottomOfEditForm = currentEditForm.getAbsoluteTop() + currentEditForm.getOffsetHeight();
			if (bottomOfEditForm > bottomOfWindow) {
				currentEditForm.makeButtonsStick(true);
			} else {
				currentEditForm.makeButtonsStick(false);
			}
		}
	}

}

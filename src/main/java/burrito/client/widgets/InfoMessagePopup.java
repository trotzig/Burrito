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

package burrito.client.widgets;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import burrito.client.widgets.InfoMessagePopup;

/**
 * This popup can be used to show fading messages. The popup will be displayed
 * centered at the very top of the page.
 * 
 * @author henper
 * 
 */
public class InfoMessagePopup extends PopupPanel {

	private Animation animation = new Animation() {

		@Override
		protected void onUpdate(double progress) {
			double d = 1.0d - progress;
			int timesAHoundred = (int) (d * 100.0d);
			if (d == 0.0d) {
				InfoMessagePopup.this.hide();
			}
			Style style = InfoMessagePopup.this.getElement().getStyle();
			style.setProperty("opacity", String.valueOf(d));
			style
					.setProperty("filter", "alpha(opacity=" + timesAHoundred
							+ ")");
		}
	};

	private Label label = new Label();

	public InfoMessagePopup() {
		setWidget(label);
		addStyleName("k5-InfoMessage");
	}

	private void position() {
		int w = Window.getClientWidth();
		int left = (w / 2) - 100;
		int scrollTop = Window.getScrollTop();
		setPopupPosition(left, scrollTop);
	}

	/**
	 * Will show this panel with the specified message. The panel will fade out
	 * during 5 seconds
	 * 
	 * @param message
	 */
	public void setTextAndShow(String message) {
		position();
		label.setText(message);
		show();
		animation.run(5000);
	}

}

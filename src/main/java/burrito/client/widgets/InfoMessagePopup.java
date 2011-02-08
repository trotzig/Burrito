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

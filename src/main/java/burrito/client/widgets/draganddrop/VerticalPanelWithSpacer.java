package burrito.client.widgets.draganddrop;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VerticalPanelWithSpacer extends VerticalPanel {

	private static final String CSS_CLASS = "vertical-spacer";
	
	public VerticalPanelWithSpacer() {
		Label spacer = new Label("");
		spacer.setStyleName(CSS_CLASS);
		super.add(spacer);
	}
	
	@Override
	public void add(Widget w) {
		super.insert(w, getWidgetCount() - 1);
	}
	
	@Override
	public void insert(Widget w, int beforeIndex) {
		if(beforeIndex == getWidgetCount()) {
			beforeIndex--;
		}
		super.insert(w, beforeIndex);
	}
}

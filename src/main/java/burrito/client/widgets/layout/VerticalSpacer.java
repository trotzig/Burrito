package burrito.client.widgets.layout;

import com.google.gwt.user.client.ui.HTML;

/**
 * Silly widget that takes up space
 * 
 * @author henper
 * 
 */
public class VerticalSpacer extends HTML {

	public VerticalSpacer(int height) {
		super("&nbsp;");
		setHeight(height + "px");
	}
	
}

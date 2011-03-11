package burrito.client.crud.widgets;

import com.google.gwt.user.client.ui.RichTextArea.Formatter;
import com.google.gwt.user.client.ui.Widget;

/**
 * A plugin for the crud rich text area
 * 
 * @author henper
 * 
 */
public interface CrudRichTextPlugin {

	/**
	 * Gets a widget (a button or similar) to use as a controller for the rich
	 * text area.
	 * 
	 * @param formatter
	 * @return
	 */
	Widget getWidget(Formatter formatter);

}

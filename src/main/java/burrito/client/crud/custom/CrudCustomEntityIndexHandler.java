package burrito.client.crud.custom;

import burrito.client.crud.CrudPanel;

import com.google.gwt.user.client.ui.Widget;

/**
 * Interface that describes how a custom entity index page is created. Use it to
 * register custom entity index pages by calling {@link CrudPanel}
 * .registerCustomEntityIndex().
 * 
 * @author henper
 * 
 */
public interface CrudCustomEntityIndexHandler {

	/**
	 * Creates a widget that is used to list entities. 
	 * @return
	 */
	Widget createEntityIndex();

}

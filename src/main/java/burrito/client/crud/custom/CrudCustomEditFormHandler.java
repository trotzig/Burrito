package burrito.client.crud.custom;

import burrito.client.crud.CrudPanel;
import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.input.CrudInputFieldWrapper;

import com.google.gwt.user.client.ui.Widget;

/**
 * Interface that describes how a custom edit form is created. Use it to
 * register custom edit forms by calling {@link CrudPanel}
 * .registerCustomEditForm().
 * 
 * @author henper
 * 
 */
public interface CrudCustomEditFormHandler {

	/**
	 * Creates a widget that is used to edit entities. Hint: Use
	 * {@link CrudEditFormHelper} in conjunction with
	 * {@link CrudInputFieldWrapper} to quickly get validation and save support.
	 * 
	 * @param desc
	 * @return
	 */
	Widget createEditForm(CrudEntityDescription desc);

}

package burrito.client.crud;

import burrito.client.crud.generic.CrudField;
import burrito.client.crud.input.CrudInputField;

/**
 * Handler that can inject specific admin widgets to the CRUD admin area.
 * 
 * @author henper
 * 
 */
public interface CrudFieldPluginHandler {

	CrudInputField<?> process(CrudField field);
	
}

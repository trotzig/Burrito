package burrito.services;

import java.lang.reflect.Field;

import burrito.client.crud.generic.CrudField;

public interface PluginCrudHandler {

	/**
	 * Processes a field
	 * 
	 * @param f
	 * @param entity 
	 * @return
	 */
	CrudField process(Field f, Object entity);

}

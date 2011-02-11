package burrito.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import burrito.client.crud.generic.CrudField;

public class PluginCrudManager {

	private static PluginCrudManager instance;

	public static PluginCrudManager get() {
		if (instance == null) {
			instance = new PluginCrudManager();
		}
		return instance;
	}

	private List<PluginCrudHandler> handlers = new ArrayList<PluginCrudHandler>();

	private PluginCrudManager() {
		// private constructor
	}

	/**
	 * Adds a plugin handler
	 * 
	 * @param handler
	 */
	public void addCrudHandler(PluginCrudHandler handler) {
		handlers.add(handler);
	}

	/**
	 * Processes all plugin handlers
	 * 
	 * @param f
	 * @param entity 
	 * @return
	 */
	public CrudField process(Field f, Object entity) {
		for (PluginCrudHandler h : handlers) {
			CrudField cf = h.process(f, entity);
			if (cf != null) {
				return cf;
			}
		}
		return null;
	}

}

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

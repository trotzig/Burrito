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

package burrito;

import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import taco.Controller;

public class MessagesController implements Controller<Map<String, String>> {

	@Override
	public Map<String, String> execute() {
		ResourceBundle bundle = getResourceBundle();
		Enumeration<String> keys = bundle.getKeys();
		Map<String, String> messages = new HashMap<String, String>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String msg = bundle.getString(key);
			messages.put(key, msg);
		}

		return messages;
	}

	private ResourceBundle getResourceBundle() {
		try {
			PropertyResourceBundle rb = new PropertyResourceBundle(
					new InputStreamReader(this.getClass().getResourceAsStream(
							"/burrito/BurritoMessages.properties"), "UTF-8"));
			return rb;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

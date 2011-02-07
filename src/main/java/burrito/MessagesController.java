package burrito;

import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import mvcaur.Controller;

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

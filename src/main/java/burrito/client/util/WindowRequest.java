package burrito.client.util;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.TextBox;

public class WindowRequest {

	FormPanel form = new FormPanel("_blank");

	public WindowRequest(String url) {
		form.setAction(url);
	}

	public void addParam(String name, String value) {
		TextBox textBox = new TextBox();
		textBox.setName(name);
		textBox.setValue(value);
		form.add(textBox);
	}

	public void post() {
		form.setMethod(FormPanel.METHOD_POST);
		form.submit();
	}
}

package burrito.client.crud.input;

import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.BBCodeField;
import burrito.client.crud.widgets.BBCodeEditor;

import com.google.gwt.user.client.ui.Widget;

public class BBCodeInputField implements CrudInputField<String> {

	private final BBCodeField field;
	private BBCodeEditor codeEditor;

	public BBCodeInputField(BBCodeField field) {
		this.field = field;
		this.codeEditor = new BBCodeEditor((String) field.getValue());
	}

	@Override
	public Widget getDisplayWidget() {
		return codeEditor;
	}

	@Override
	public void load(String value) {
		codeEditor.setText(value);
	}

	@Override
	public String getValue() {
		return codeEditor.getText();
	}

	@Override
	public CrudField getCrudField() {
		field.setValue(codeEditor.getText());
		return field;
	}

}

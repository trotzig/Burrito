package burrito.client.crud.input;


import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.StringSelectionField;

import com.google.gwt.user.client.ui.Widget;
import com.kanal5.play.client.widgets.selection.SelectionList;
import com.kanal5.play.client.widgets.selection.SelectionListLabelCreator;

@SuppressWarnings("rawtypes")
public class StringSelectionListField implements CrudInputField {

	private StringSelectionField field;
	private SelectionList<String> selectionList;

	public StringSelectionListField(final StringSelectionField field) {
		this.field = field;
		this.selectionList = new SelectionList<String>(field.isRequired());
		this.selectionList.setNullSelectLabel("");
		this.selectionList
				.setLabelCreator(new SelectionListLabelCreator<String>() {

					@Override
					public String createLabel(String obj) {
						return field.getLabel(obj);
					}
				});
		load(field.getValue());
		selectionList.setModel(field.getValues());
		selectionList.render();

	}

	public CrudField getCrudField() {
		field.setValue(selectionList.getValue());
		return field;
	}

	public Widget getDisplayWidget() {
		return selectionList;
	}

	public Object getValue() {
		return selectionList.getValue();
	}

	public void load(Object value) {
		selectionList.setValue((String) value);
	}

}

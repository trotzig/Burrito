package burrito.client.crud.input;

import java.util.List;



import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.EnumField;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("rawtypes")
public class EnumSelectionListField implements CrudInputField {

	private static CrudServiceAsync service = GWT.create(CrudService.class);

	private SelectionList<String> selectionList;

	private EnumField field;

	public EnumSelectionListField(final EnumField field) {
		this.field = field;
		selectionList = new SelectionList<String>(field.isRequired());
		selectionList.setNullSelectLabel("");
		selectionList.setLabelCreator(new SelectionListLabelCreator<String>() {

			@Override
			public String createLabel(String obj) {
				return CrudLabelHelper.getString((field.getTypeClassName()
						+ "_" + obj.toString()).replace('.', '_'));
			}
		});
		load(field.getValue());
		init();
	}

	private void init() {
		service.getEnumListValues(field.getTypeClassName(),
				new AsyncCallback<List<String>>() {

					public void onSuccess(List<String> result) {
						selectionList.setModel(result);
						selectionList.render();
					}

					public void onFailure(Throwable caught) {
						throw new RuntimeException(
								"Failed to get list values of type "
										+ field.getType(), caught);
					}
				});
	}

	public CrudField getCrudField() {
		field.setValue(getValue());
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

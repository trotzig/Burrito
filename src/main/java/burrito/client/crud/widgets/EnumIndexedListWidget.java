package burrito.client.crud.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.widgets.validation.HasValidators;
import burrito.client.widgets.validation.InputFieldValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class EnumIndexedListWidget extends Composite implements HasValidators {

	private static final int LABEL_COLUMN = 0;
	private static final int TEXT_BOX_COLUMN = 1;
	private static final int ASTERISK_COLUMN = 2;

	private final boolean required;
	private final String enumClassName;

	private final CrudServiceAsync service = GWT.create(CrudService.class);

	private Grid grid = new Grid(1, 3);
	private int rowCount = 0;
	private List<String> startValue;

	public EnumIndexedListWidget(boolean required, String enumClassName) {
		this.required = required;
		this.enumClassName = enumClassName;

		initWidget(grid);

		service.getEnumListValues(enumClassName, new AsyncCallback<List<String>>() {
			@Override
			public void onSuccess(List<String> enumValues) {
				createFields(enumValues);
			}

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}
		});
	}

	private void createFields(List<String> enumValues) {
		rowCount = enumValues.size();
		grid.resizeRows(rowCount);

		String enumClassNameWithUnderscore = enumClassName.replace(".", "_");

		for (int row = 0; row < rowCount; row++) {
			String labelText = CrudLabelHelper.getString(enumClassNameWithUnderscore + "_" + enumValues.get(row));

			grid.setWidget(row, LABEL_COLUMN, new Label(labelText + ":"));
			grid.setWidget(row, TEXT_BOX_COLUMN, new TextBox());

			if (required) {
				grid.setWidget(row, ASTERISK_COLUMN, new Label("*"));
			}
		}

		if (startValue != null) {
			renderStartValue();
		}
	}

	public void setValue(List<String> value) {
		startValue = value;
		if(startValue == null) {
			startValue = Collections.emptyList();
		}
		
		renderStartValue();
	}

	private void renderStartValue() {
		for (int row = 0; row < rowCount; row++) {
			String text = row < startValue.size() ? startValue.get(row) : null;
			getTextBoxForRow(row).setValue(text == null ? "" : text);
		}
	}

	private TextBox getTextBoxForRow(int row) {
		return (TextBox) grid.getWidget(row, TEXT_BOX_COLUMN);
	}

	public List<String> getValue() {
		if (startValue == null) {
			throw new RuntimeException("Too soon for value");
		}

		if (rowCount > 0) {
			List<String> value = new ArrayList<String>(rowCount);

			for (int row = 0; row < rowCount; row++) {
				String text = getTextBoxForRow(row).getValue(); 
				value.add(text.isEmpty() ? null : text);
			}

			return value;
		}

		return startValue;
	}

	@Override
	public void addInputFieldValidator(InputFieldValidator validator) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean validate() {
		if (required) {
			for (String s : getValue()) {
				if (s == null) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void setValidationError(String validationError) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void highlight() {
		for (int row = 0; row < rowCount; row++) {
			TextBox textBox = getTextBoxForRow(row);

			if (textBox.getValue() == null) {
				textBox.setFocus(true);
				return;
			}
		}
	}
}

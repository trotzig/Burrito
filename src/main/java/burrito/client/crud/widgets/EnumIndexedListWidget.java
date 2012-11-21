package burrito.client.crud.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.widgets.date.DateTimePickerWidget;
import burrito.client.widgets.validation.HasValidators;
import burrito.client.widgets.validation.InputFieldValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EnumIndexedListWidget<T> extends Composite implements HasValidators {

	private static final int LABEL_COLUMN = 0;
	private static final int TEXT_BOX_COLUMN = 1;
	private static final int ASTERISK_COLUMN = 2;

	private final boolean required;
	private final String enumClassName;

	private final CrudServiceAsync service = GWT.create(CrudService.class);

	private Grid grid = new Grid(1, 3);
	private int rowCount = 0;
	private List<Object> startValue;
	private String valueListClassName;

	public EnumIndexedListWidget(boolean required, String enumClassName, String listValueClassName) {
		this.required = required;
		this.enumClassName = enumClassName;
		this.valueListClassName = listValueClassName;
		
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
			grid.setWidget(row, TEXT_BOX_COLUMN, createInputWidget());

			if (required) {
				grid.setWidget(row, ASTERISK_COLUMN, new Label("*"));
			}
		}

		if (startValue != null) {
			renderStartValue();
		}
	}
	
	private Widget createInputWidget() {
		if (valueListClassName.equals("java.lang.String")) {
			return new TextBox();
		} else if (valueListClassName.equals("java.util.Date")) {
			return new DateTimePickerWidget(this.required);
		}
		
		return null;
	}
	
	private void setWidgetValue(int row, Object value) {
		if (valueListClassName.equals("java.lang.String")) {
			TextBox textbox = (TextBox) grid.getWidget(row, TEXT_BOX_COLUMN);
			if (value == null) {
				textbox.setValue("");
			} else  {
				textbox.setValue((String) value);
			}
			
		} else if (valueListClassName.equals("java.util.Date")) {
			DateTimePickerWidget dateWidget = (DateTimePickerWidget) grid.getWidget(row, TEXT_BOX_COLUMN);
			dateWidget.setDate((Date) value);
		}
	}
	
	private Object getWidgetValue(int row) {
		if (valueListClassName.equals("java.lang.String")) {
			TextBox textbox = (TextBox) grid.getWidget(row, TEXT_BOX_COLUMN);
			return textbox.getValue();
			
		} else if (valueListClassName.equals("java.util.Date")) {
			DateTimePickerWidget dateWidget = (DateTimePickerWidget) grid.getWidget(row, TEXT_BOX_COLUMN);
			return dateWidget.getDate();
		}
		
		return null;
	}
	
	public void setValue(List<Object> value) {
		startValue = value;
		if(startValue == null) {
			startValue = Collections.emptyList();
		}
		
		renderStartValue();
	}

	private void renderStartValue() {
		for (int row = 0; row < rowCount; row++) {
			Object value = row < startValue.size() ? startValue.get(row) : null;
			setWidgetValue(row, value);
		}
	}

	public List<Object> getValue() {
		if (startValue == null) {
			throw new RuntimeException("Too soon for value");
		}

		if (rowCount > 0) {
			List<Object> value = new ArrayList<Object>(rowCount);

			for (int row = 0; row < rowCount; row++) {
				Object widgetValue = getWidgetValue(row);
				value.add(widgetValue);
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
			for (Object s : getValue()) {
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
//		
//		for (int row = 0; row < rowCount; row++) {
//			Object value = getWidgetValue(row);
//
//			if (value == null) {
//				getWidget(row).set
//				return;
//			}
//		}
	}
}

package burrito.client.crud.input;

import java.util.Date;


import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.DateField;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.kanal5.play.client.widgets.date.DateTimePickerWidget;

public class DateCrudInputField implements CrudInputField<Date> {

	private DateField field;
	private DateTimePickerWidget datePicker;
	private Label label;

	public DateCrudInputField(DateField field) {
		this.field = field;
		Date value = (Date) field.getValue();
		if (field.isReadOnly()) {
			label = new Label(value != null ? value.toString() : "Not set");
		}
		else {
			datePicker = new DateTimePickerWidget(field.isRequired());
			load(value);
		}
	}

	public Widget getDisplayWidget() {
		if (datePicker != null) return datePicker;
		return label;
	}

	public Date getValue() {
		if (datePicker != null) return datePicker.getDate();
		return (Date) field.getValue();
	}

	public void load(Date value) {
		if (datePicker != null) datePicker.setDate(value);
	}

	public CrudField getCrudField() {
		field.setValue(getValue());
		return field;
	}
}

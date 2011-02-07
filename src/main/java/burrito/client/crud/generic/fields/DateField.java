package burrito.client.crud.generic.fields;

import java.util.Date;

import burrito.client.crud.generic.CrudField;




/**
 * A {@link CrudField} representing a {@link Date}
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class DateField extends CrudField {

	private Date date;
	private boolean readOnly;

	public DateField() {
		// default constructor
	}

	public DateField(Date date) {
		this(date, false);
	}

	public DateField(Date date, boolean readOnly) {
		super();
		this.date = date;
		this.readOnly = readOnly;
	}

	@Override
	public Class<?> getType() {
		return Date.class;
	}

	@Override
	public Object getValue() {
		return date;
	}

	@Override
	public void setValue(Object value) {
		this.date = (Date) value;
	}

	public boolean isReadOnly() {
		return readOnly;
	}
}

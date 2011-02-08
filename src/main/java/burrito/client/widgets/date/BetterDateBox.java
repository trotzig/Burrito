package burrito.client.widgets.date;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;

public class BetterDateBox extends DateBox implements HasChangeHandlers {

	private DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
	private List<ChangeHandler> changeHandlers = new ArrayList<ChangeHandler>();

	public BetterDateBox() {
		super();

		getTextBox().setReadOnly(true);

		setFormat(new Format() {
			@Override
			public void reset(DateBox dateBox, boolean abandon) {
			}

			@Override
			public Date parse(DateBox dateBox, String text, boolean reportError) {
				try {
					return dtf.parse(text);
				}
				catch (Exception e) {
					return null;
				}
			}

			@Override
			public String format(DateBox dateBox, Date date) {
				if (date == null) return "";
				return dtf.format(date);
			}
		});

		addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				for (ChangeHandler handler : changeHandlers) {
					handler.onChange(null);
				}
			}
		});
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		changeHandlers.add(handler);
		return null;
	}
}

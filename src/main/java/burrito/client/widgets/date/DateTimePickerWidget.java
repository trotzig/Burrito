package burrito.client.widgets.date;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import burrito.client.widgets.date.BetterDateBox;
import burrito.client.widgets.date.DateTimePickerMessages;
import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;
import burrito.client.widgets.validation.HasValidators;
import burrito.client.widgets.validation.InputFieldValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Widget that allows selection of date and time (hour/minutes).
 * 
 * @author joasod
 * 
 */
public class DateTimePickerWidget extends Composite implements HasChangeHandlers, HasValidators {

	public static final String HTML_ELEMENT_ID = "gwt-dateTimePicker-widget";
	private Grid wrapper = new Grid(3, 3);
	private BetterDateBox dateBox = new BetterDateBox();
	private HorizontalPanel timeWrapper = new HorizontalPanel();
	private SelectionList<Integer> hourSelector = new SelectionList<Integer>(false);
	private SelectionList<Integer> minuteSelector = new SelectionList<Integer>(false);
	private Date date;
	private Anchor clear;
	private Label requiredStar = new Label("*");
	private Label errorMessage;
	private DateTimePickerMessages messages = GWT
			.create(DateTimePickerMessages.class);
	private List<ChangeHandler> changeHandlers = new ArrayList<ChangeHandler>();
	private boolean required;
	
	public DateTimePickerWidget() {
		this(false);
	}
	
	public DateTimePickerWidget(boolean required) {
		initWidget(wrapper);
		this.required = required;

		List<Integer> hoursModel = new ArrayList<Integer>();
		for (int hour = 0; hour <= 23; hour++) {
			hoursModel.add(hour);
		}
		hourSelector.setModel(hoursModel);
		hourSelector.setLabelCreator(labelCreator);
		hourSelector.addChangeHandler(dateTimeChangeHandler);
		hourSelector.render();

		List<Integer> minutesModel = new ArrayList<Integer>();
		for (int minute = 0; minute <= 59; minute++) {
			minutesModel.add(minute);
		}
		minuteSelector.setModel(minutesModel);
		minuteSelector.setLabelCreator(labelCreator);
		minuteSelector.addChangeHandler(dateTimeChangeHandler);
		minuteSelector.render();

		clear = new Anchor(messages.clear());
		clear.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearFields();
			}
		});
		
		timeWrapper.add(hourSelector);
		timeWrapper.add(new Label(" : "));
		timeWrapper.add(minuteSelector);
		
		wrapper.addStyleName("k5-DateTimePicker-wrapper");
		dateBox.addStyleName("k5-DateTimePicker-datepicker");
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				update(event.getValue());
			}
		});
		dateBox.getTextBox().setTitle(messages.dateHelpText());

		Label dateHeader = new Label(messages.date());
		dateHeader.addStyleName("k5-DateTimePicker-dateHeader");
		Label timeHeader = new Label(messages.time());
		timeHeader.addStyleName("k5-DateTimePicker-timeheader");

		errorMessage = new Label();
		errorMessage.addStyleName("validationError");
		
		wrapper.clear();
		wrapper.setWidget(0, 0, dateHeader);
		wrapper.setWidget(0, 1, timeHeader);
		wrapper.setWidget(1, 0, dateBox);
		wrapper.setWidget(1, 1, timeWrapper);
		if(required) {
			wrapper.setWidget(1, 2, requiredStar);
		}
		wrapper.setWidget(2, 0, clear);
		setVisible(true);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
		updateWidgets();
	}

	@SuppressWarnings("deprecation")
	// required by GWT
	private void updateWidgets() {
		dateBox.setValue(date);
		if (date == null) {
			hourSelector.setValue(0);
			minuteSelector.setValue(0);
		} else {
			hourSelector.setValue(date.getHours());
			minuteSelector.setValue(date.getMinutes());
		}
	}
	
	private void clearFields() {
		date = null;
		minuteSelector.setValue(null);
		hourSelector.setValue(null);
		updateWidgets();
	}


	@SuppressWarnings("deprecation")
	// required by GWT
	private void update(Date eventDate) {
		if(eventDate == null){
			eventDate = date;
		}
		eventDate.setHours(hourSelector.getValue());
		eventDate.setMinutes(minuteSelector.getValue());
		eventDate.setSeconds(0);
		date = eventDate;
		for (ChangeHandler handler : changeHandlers) {
			handler.onChange(null);
		}
	}

	private ChangeHandler dateTimeChangeHandler = new ChangeHandler() {

		@Override
		public void onChange(ChangeEvent event) {
			update(dateBox.getValue());
		}
	};

	/**
	 * Pads numbers below 10 with a leading 0.
	 */
	private SelectionListLabelCreator<Integer> labelCreator = new SelectionListLabelCreator<Integer>() {

		@Override
		public String createLabel(Integer value) {
			StringBuilder sb = new StringBuilder();
			if (value < 10) {
				sb.append("0");
			}
			sb.append(value);
			return sb.toString();

		}
	};

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		changeHandlers.add(handler);
		//Lazily returning null...
		return null;
	}

	@Override
	public void addInputFieldValidator(InputFieldValidator validator) {
		//behaviour disabled because the InputFieldValidator interface is not suitable for type Date
	}

	@Override
	public void setValidationError(String validationError) {
		if(validationError == null) {
			errorMessage.setText(null);
			wrapper.clearCell(1, 2);
		} else {
			errorMessage.setText(validationError);
			wrapper.setWidget(1, 2, errorMessage);
		}
	}

	@Override
	public boolean validate() {
		if(required && date == null) {
			setValidationError(messages.requiredError());
			return false;
		} else {
			setValidationError(null);
			return true;
		}
	}
}

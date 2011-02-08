package burrito.client.widgets.selection;

import java.util.Date;

import burrito.client.widgets.selection.IntegerIntervalSelectionList;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

/**
 * {@link IntegerIntervalSelectionList} which displays years from 1900 up to current year.
 * Selection is preset to MAX_YEAR - 20.
 * 
 * @author joasod
 *
 */
@SuppressWarnings("deprecation")
public class BirthYearSelectionList extends IntegerIntervalSelectionList{

	private static int MIN_YEAR = 1900;
	private static int MAX_YEAR = MIN_YEAR + new Date().getYear(); //Required by GWT
	private CommonMessages messages = GWT.create(CommonMessages.class);
	
	public BirthYearSelectionList(boolean required) {
		super(required);
		setMin(MIN_YEAR);
		setMax(MAX_YEAR);
		setNullSelectLabel(messages.selectBirthYear());
		setReverse(true);
		render();
	}
}

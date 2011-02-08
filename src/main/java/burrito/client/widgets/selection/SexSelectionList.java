package burrito.client.widgets.selection;

import java.util.ArrayList;
import java.util.List;

import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;
import burrito.client.widgets.selection.Sex;

import com.google.gwt.core.client.GWT;
import burrito.client.widgets.messages.CommonMessages;

/**
 * Selection list for sexes
 * 
 * @author henper
 * 
 */
public class SexSelectionList extends SelectionList<Sex> {

	private CommonMessages messages = GWT.create(CommonMessages.class);

	public SexSelectionList(boolean required) {
		super(required);
		List<Sex> sexes = new ArrayList<Sex>();
		for (Sex sex : Sex.values()) {
			sexes.add(sex);
		}
		SelectionListLabelCreator<Sex> lc = new SelectionListLabelCreator<Sex>() {

			@Override
			public String createLabel(Sex obj) {
				if (obj == Sex.FEMALE) {
					return messages.sexFemale();
				} else {
					return messages.sexMale();
				}
			}
		};
		setModel(sexes);
		setLabelCreator(lc);
		setNullSelectLabel(messages.selectSex());
		render();
	}
}

package burrito.client.crud;


import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The top header for the automatic GWT CRUD module. Has a heading and a
 * breadcrumb section.
 * 
 * @author henper
 * 
 */
public class CrudPanelTop extends Composite {

	private VerticalPanel wrapper = new VerticalPanel();
	private CrudMessages messages = GWT.create(CrudMessages.class);
	private HorizontalPanel breadCrumbs = new HorizontalPanel();

	public CrudPanelTop() {
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.addStyleName("k5-CrudPanelTop-breadcrumbs");
		simplePanel.setWidget(breadCrumbs);
		wrapper.add(simplePanel);
		initWidget(wrapper);
		addStyleName("k5-CrudPanelTop");
		setWidth("100%");
	}

	public void update(String entityName, String entityDisplayString) {
		String entityNameDisplay = null;
		if (entityName == null) {
			breadCrumbs.clear();
			breadCrumbs.setVisible(false);
			return;
		}
		entityNameDisplay = CrudLabelHelper.getString(entityName.replace('.',
				'_'));
		breadCrumbs.clear();
		breadCrumbs.add(new Hyperlink(messages.admin(), ""));
		breadCrumbs.add(new HTML("&nbsp;>&nbsp;"));
		if (entityDisplayString != null) {
			breadCrumbs.add(new Hyperlink(entityNameDisplay, entityName));
			breadCrumbs.add(new HTML("&nbsp;>&nbsp;"));
			breadCrumbs.add(new Label(entityDisplayString));
		} else {
			breadCrumbs.add(new Label(entityNameDisplay));
		}
		breadCrumbs.setVisible(true);
	}

}

package burrito.client.sitelet;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kanal5.play.client.widgets.layout.VerticalSpacer;

public class SiteletAdminPanel extends Composite implements ValueChangeHandler<String> {

	private VerticalPanel wrapper = new VerticalPanel();
	private SimplePanel contentPlaceHolder = new SimplePanel();
	private String containerName;

	public SiteletAdminPanel(String containerName) {
		this.containerName = containerName;
		wrapper.add(new VerticalSpacer(20));
		wrapper.add(contentPlaceHolder);
		contentPlaceHolder.addStyleName("k5-SiteletAdminPanel-content");
		initWidget(wrapper);
		addStyleName("k5-SiteletAdminPanel");
		setWidth("100%");
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		String historyToken = event.getValue();
		contentPlaceHolder.setWidget(createContentWidget(historyToken));
	}

	private Widget createContentWidget(String historyToken) {
		if ("".equals(historyToken)) {
			return new SiteletOverviewPanel(containerName);
		} 
		try {
			Long id = Long.parseLong(historyToken);
			return new SiteletEditPanel(containerName, id);
		} catch(NumberFormatException e) {
			throw new RuntimeException("Unknown history token: " + historyToken);
		}
		
	}

}

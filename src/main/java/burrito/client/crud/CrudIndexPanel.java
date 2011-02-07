package burrito.client.crud;

import java.util.List;

import burrito.client.crud.generic.CrudEntityInfo;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.kanal5.play.client.widgets.layout.VerticalSpacer;

/**
 * Generic panel that displays all editable entities (as decided by
 * CrudServiceImpl.getAllEntities()) within the application.
 * 
 * @author henper
 * 
 */
public class CrudIndexPanel extends Composite {

	private VerticalPanel wrapper = new VerticalPanel();
	private CrudMessages messages = GWT.create(CrudMessages.class);
	private CrudServiceAsync service = GWT.create(CrudService.class);

	public CrudIndexPanel() {
		service.getAllEntities(new AsyncCallback<List<CrudEntityInfo>>() {

			public void onSuccess(List<CrudEntityInfo> result) {
				init(result);
			}

			public void onFailure(Throwable caught) {
				throw new RuntimeException("Failed to list editable entities",
						caught);
			}
		});
		initWidget(wrapper);
	}

	protected void init(List<CrudEntityInfo> result) {
		wrapper.add(new Label(messages.chooseEntity()));
		if (result.isEmpty()) {
			wrapper.add(new Label(messages.noEntitiesToEdit()));
			wrapper.add(new VerticalSpacer(10));
		}
		for (CrudEntityInfo crudEntityInfo : result) {
			HorizontalPanel fp = new HorizontalPanel();
			String underscore = crudEntityInfo.getEntityName()
					.replace('.', '_');
			fp.add(new Hyperlink(CrudLabelHelper.getString(underscore),
					crudEntityInfo.getEntityName()));
			String desc = CrudLabelHelper.getNullableString(underscore
					+ "_desc");
			if (desc != null) {
				fp.add(new HTML("&nbsp;-&nbsp;" + desc));
			}
			wrapper.add(fp);
			wrapper.add(new VerticalSpacer(10));
		}
	}

}

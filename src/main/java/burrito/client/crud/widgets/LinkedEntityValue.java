package burrito.client.crud.widgets;


import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class LinkedEntityValue extends Composite {

	private CrudServiceAsync service = GWT.create(CrudService.class);
	private String json;
	private HorizontalPanel wrapper = new HorizontalPanel();
	private Label label = new Label();
	private CrudMessages labels = GWT.create(CrudMessages.class);
	

	public LinkedEntityValue(String json) {
		this.json = json;
		wrapper.add(label);
		Anchor edit = new Anchor(labels.editLink());
		edit.addClickHandler(new ClickHandler() {
		
			public void onClick(ClickEvent event) {
				LinkedEntityWidgetPopup popup = new LinkedEntityWidgetPopup(new LinkedEntityWidgetPopup.SaveHandler() {
				
					public void saved(String json) {
						LinkedEntityValue.this.json = json;
						load();
					}
				});
				popup.fromJson(LinkedEntityValue.this.json);
				popup.center();
				popup.show();
			}
		});
		wrapper.add(new HTML("&nbsp;"));
		wrapper.add(edit);
		wrapper.add(new HTML("&nbsp;"));
		load();
		
		initWidget(wrapper);
		addStyleName("k5-LinkedEntityValue");
	}

	private void load() {
		LinkedEntityJsonOverlay overlay = asLinkedEntity(json);
		String className = overlay.getTypeClassName();
		final String linkText = overlay.getLinkText();
		final String typeLabel = CrudLabelHelper.getString(className.replace(
				'.', '_') + "_singular");
		if (LinkedEntityWidgetPopup.TYPE_ABSOLUTE_URL.equals(className)) {
			label.setText(labels.linkOutputAbsoluteUrl(linkText, overlay.getAbsoluteLink()));
	
		} else {
			service.describe(className, Long.valueOf(overlay.getTypeId()),
					null, new AsyncCallback<CrudEntityDescription>() {

						public void onSuccess(CrudEntityDescription result) {
							label.setText(labels.linkOutputUrl(linkText, typeLabel, result.getDisplayString()));
						}

						public void onFailure(Throwable caught) {
							throw new RuntimeException(
									"Failed to get entity description", caught);
						}
					});
		}

	}

	private final native LinkedEntityJsonOverlay asLinkedEntity(String json) /*-{
	return eval("json=" + json);
	}-*/;

	/**
	 * Gets the json contents of the link
	 * 
	 * @return
	 */
	public String getJson() {
		return this.json;
	}

}

package burrito.client.crud.widgets;


import burrito.client.crud.widgets.SelectableTextArea.SelectedText;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class BBPopupPanel extends DialogBox {

	private VerticalPanel panel = new VerticalPanel();
	
	private final SelectableTextArea rawEditor;
	private SelectedText selectedText;

	public BBPopupPanel(String caption, SelectableTextArea rawEditor2) {
		super();
		setText(caption);
		this.rawEditor = rawEditor2;
		this.selectedText = rawEditor2.getSelectedTextObj();
	}
	
	protected String getSelectedText() {
		return this.selectedText.text;
	}
	
	protected void setSelectedText(String text) {
		this.selectedText.text = text;
		this.rawEditor.setSelectedText(this.selectedText);
	}
	
	protected void addPanel(Widget w) {
		panel.add(w);
	}
	
	abstract protected void onShow();
	
	abstract protected boolean onClose();
	
	@Override
	public void show() {
		add(panel);
		
		onShow();
		
		Button okButton = new Button("Ok");
		okButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (onClose()) {
					hide();
				}
			}
		});
		
		Button cancelButton = new Button("Avbryt");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(okButton);
		horizontalPanel.add(cancelButton);
		addPanel(horizontalPanel);
		
		super.show();
		center();
	}
}

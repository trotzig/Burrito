package burrito.client.crud.widgets;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class BBYoutubePanel extends BBPopupPanel {

	public BBYoutubePanel(SelectableTextArea rawEditor) {
		super("Youtube-video", rawEditor);
	}

	private TextBox textBox;

	@Override
	protected void onShow() {
		Label label = new Label("src");
		textBox = new TextBox();
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(label);
		horizontalPanel.add(textBox);
		addPanel(horizontalPanel);
		
		textBox.setText(getSelectedText());
	}

	private boolean validate(String url) {
		//http://youtu.be/R7J8FSBitWs  är 11 tecken 
		return url.matches("^http://youtu.be/.{8,15}$");
	}
	
	
	@Override
	protected boolean onClose() {
		String url = textBox.getText();
		
		if (!validate(url)) {
			Window.alert("Not valid youtube url!");
			return false;
		}
		
		setSelectedText("[video]" + url + "[/video]");
		
		return true;
	}
}

package burrito.client.crud.widgets;


import burrito.client.crud.labels.CrudMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RichTextArea.Formatter;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class CrudRichTextArea extends Composite implements HasKeyDownHandlers {

	private DockPanel richTextDock = new DockPanel();
	private VerticalPanel wrapper = new VerticalPanel();
	private TabPanel tabPanel = new TabPanel();
	private RichTextArea textArea;
	private ToggleButton italics;
	private ToggleButton bold;
	private Formatter formatter;
	private TextArea raw = new TextArea();
	private boolean rawVisible;
	private ToggleButton strikethrough;
	private ToggleButton underline;
	private ImagePickerPopup imagePicker;
	private SimplePanel textAreaWrapper = new SimplePanel();
	private CrudMessages labels = GWT.create(CrudMessages.class);

	public CrudRichTextArea(String value) {
		textArea = new FormattedRichTextArea();
		textArea.setHTML(value);
		formatter = textArea.getFormatter();
		richTextDock.add(createCommandsArea(), DockPanel.NORTH);
		textArea.addKeyDownHandler(new KeyDownHandler() {

			public void onKeyDown(KeyDownEvent event) {
				toggleButtons();
				checkForKeyBoardCombinations(event);
			}
		});
		textArea.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				toggleButtons();
			}
		});
		textAreaWrapper.setWidget(textArea);
		richTextDock.add(textAreaWrapper, DockPanel.CENTER);
		HTML richTextHeader = new HTML(labels.richText());
		richTextHeader.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (rawVisible) {
					textArea.setHTML(raw.getText());
					textAreaWrapper.clear();
					textAreaWrapper.setWidget(textArea);
				}
				rawVisible = false;
			}
		});
		tabPanel.add(richTextDock, richTextHeader);
		HTML rawHeader = new HTML(labels.rawHtml());
		rawHeader.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (!rawVisible) {
					raw.setText(textArea.getHTML());
				}
				rawVisible = true;
			}
		});
		tabPanel.add(raw, rawHeader);

		imagePicker = new ImagePickerPopup(800, 800, false);
		imagePicker.addSaveHandler(new ImagePickerPopup.SaveHandler() {

			public void saved(String value) {
				formatter.insertImage("/blobstore/image?key=" + value);
			}
		});
		
		wrapper.add(tabPanel);

		ToggleButton maximize = new ToggleButton(labels.maximize(), labels
				.minimize());
		maximize.addClickHandler(new ClickHandler() {
			boolean max = false;

			public void onClick(ClickEvent event) {
				if (max) {
					raw.setSize("400px", "250px");
					textArea.setSize("400px", "250px");

				} else {
					raw.setSize("800px", "440px");
					textArea.setSize("800px", "440px");
				}
				max = !max;
			}
		});
		maximize.setWidth("80px");
		wrapper.add(maximize);
		initWidget(wrapper);
		addStyleName("k5-CrudRichTextArea");
		raw.setSize("400px", "250px");
		textArea.setSize("400px", "250px");
		tabPanel.selectTab(0);
	}

	protected void checkForKeyBoardCombinations(KeyDownEvent event) {
	}

	protected void toggleButtons() {
		bold.setDown(formatter.isBold());
		italics.setDown(formatter.isItalic());
		strikethrough.setDown(formatter.isStrikethrough());
		underline.setDown(formatter.isUnderlined());
	}

	private Widget createCommandsArea() {
		HorizontalPanel hp = new HorizontalPanel();
		this.bold = new ToggleButton(new Image(
				GWT.getModuleBaseURL() + "images/format-text-bold.png"), new ClickHandler() {

			public void onClick(ClickEvent event) {
				formatter.toggleBold();
			}
		});
		bold.setTitle(labels.bold());
		hp.add(bold);

		this.italics = new ToggleButton(new Image(
				GWT.getModuleBaseURL() + "images/format-text-italic.png"), new ClickHandler() {

			public void onClick(ClickEvent event) {
				formatter.toggleItalic();
			}
		});
		italics.setTitle(labels.italic());
		hp.add(italics);

		this.strikethrough = new ToggleButton(new Image(
				GWT.getModuleBaseURL() + "images/format-text-strikethrough.png"),
				new ClickHandler() {

					public void onClick(ClickEvent event) {
						formatter.toggleStrikethrough();
					}
				});
		strikethrough.setTitle(labels.strikethrough());
		hp.add(strikethrough);

		this.underline = new ToggleButton(new Image(
				GWT.getModuleBaseURL() + "images/format-text-underline.png"), new ClickHandler() {

			public void onClick(ClickEvent event) {
				formatter.toggleUnderline();
			}
		});
		underline.setTitle(labels.underline());
		hp.add(underline);

		PushButton image = new PushButton(new Image(
				GWT.getModuleBaseURL() + "images/image-x-generic.png"), new ClickHandler() {

			public void onClick(ClickEvent event) {
				imagePicker.center();
				imagePicker.show();
			}
		});
		image.setTitle(labels.insertImage());
		hp.add(image);

		PushButton link = new PushButton(labels.link(), new ClickHandler() {

			public void onClick(ClickEvent event) {
				formatter.createLink(Window
						.prompt(labels.pasteLinkHere(), null));
			}
		});
		link.setTitle(labels.insertLink());
		hp.add(link);

		return hp;

	}

	public String getValue() {
		if (rawVisible) {
			return raw.getText();
		}
		return textArea.getHTML();
	}

	public void setValue(String value) {
		textArea.setHTML(value);
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return textArea.addKeyDownHandler(handler);
	}

}

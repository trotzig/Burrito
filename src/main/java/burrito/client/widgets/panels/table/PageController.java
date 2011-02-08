package burrito.client.widgets.panels.table;

import java.util.ArrayList;
import java.util.List;

import burrito.client.widgets.panels.table.PageController;
import burrito.client.widgets.panels.table.PageControllerHandler;
import burrito.client.widgets.panels.table.TableMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;

/**
 * A page controller is a widget that separates a collection in pages. It has no
 * knowledge of the total amount of pages, only if there is at least one more to
 * fetch.
 * 
 * @author henper
 * 
 */
public class PageController extends Composite {

	private List<PageControllerHandler> handlers = new ArrayList<PageControllerHandler>();
	private int currentPageZeroIndexed = 0;
	private TableMessages messages = GWT.create(TableMessages.class);
	private Anchor currentPageLabel;
	private PushButton next;
	private PushButton previous;

	/**
	 * Creates a {@link PageController} without a visible "Page:" label
	 */
	public PageController() {
		this(false);
	}


	/**
	 * Creates a new pagecontroller
	 * 
	 * @param showPageLabel
	 * @param maxPagesShown
	 *            If not <code>null</code> then this will be the maximum number
	 *            of pages shown in the page controller.
	 */
	public PageController(boolean showPageLabel) {
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		if (showPageLabel) {
			Label label = new Label(messages.page());
			label.addStyleName("k5-PageController-label");
			wrapper.add(label);
		}
		next = new PushButton(new Image(GWT.getModuleBaseURL() + "page-next.png?v=2"));
		previous = new PushButton(new Image(GWT.getModuleBaseURL() + "page-previous.png?v=2"));
		next.getUpDisabledFace().setImage(new Image(GWT.getModuleBaseURL() + "page-next-disabled.png?v=2"));
		previous.getUpDisabledFace().setImage(new Image(GWT.getModuleBaseURL() + "page-previous-disabled.png?v=2"));
		next.addStyleName("k5-PageController-nextButton");
		previous.addStyleName("k5-PageController-previousButton");
		currentPageLabel = createCurrentPageAnchor();
		currentPageLabel.addStyleName("k5-PageController-link-selected");
		next.setEnabled(false);
		previous.setEnabled(false);
		next.setTitle(messages.next());
		previous.setTitle(messages.previous());
		wrapper.add(previous);
		wrapper.add(currentPageLabel);
		wrapper.add(next);
		next.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (PageControllerHandler handler : handlers) {
					handler.loadPage(currentPageZeroIndexed + 1);
				}
			}
		});
		previous.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (PageControllerHandler handler : handlers) {
					handler.loadPage(currentPageZeroIndexed - 1);
				}
			}
		});
		initWidget(wrapper);
		addStyleName("k5-PageController");
		setVisible(false);
	}

	private Anchor createCurrentPageAnchor() {
		Anchor a = new Anchor();
		a.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (PageControllerHandler handler : handlers) {
					handler.loadPage(currentPageZeroIndexed);
				}
			}
		});
		a.addStyleName("k5-PageController-link");
		return a;
	}

	

	/**
	 * Adds a listener to page events
	 * 
	 * @param handler
	 */
	public void addPageControllerHandler(PageControllerHandler handler) {
		handlers.add(handler);
	}

	/**
	 * Updates the links based on number of pages and current page
	 * 
	 */
	public void update(int totalPages, final int currentPageZeroIndexed) {
		if (totalPages < 2) {
			setVisible(false);
			return;
		}
		this.currentPageZeroIndexed = currentPageZeroIndexed;
		currentPageLabel.setText(String.valueOf(currentPageZeroIndexed + 1));
		setVisible(true);
		next.setEnabled(totalPages > currentPageZeroIndexed + 1);
		previous.setEnabled(currentPageZeroIndexed - 1 >= 0);
		currentPageLabel.setTitle(messages.showPage(currentPageZeroIndexed + 1));
	}


	/**
	 * Gets the current page number
	 * 
	 * @return
	 */
	public int getCurrentPage() {
		return currentPageZeroIndexed;
	}

}

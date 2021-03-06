package burrito.client.reindex;

import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.generic.CrudEntityInfo;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.widgets.panels.table.PageMetaData;
import burrito.client.widgets.selection.SelectionList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ReindexEntityPanel extends Composite {

	private static ReindexEntityPanelUiBinder uiBinder = GWT
			.create(ReindexEntityPanelUiBinder.class);

	private static CrudMessages messages = GWT.create(CrudMessages.class);
	
	
	static int BATCH_SIZE = 100;
	
	@UiField DivElement name;
	@UiField DivElement progress;
	@UiField DivElement progressInner;
	@UiField DivElement progressText;
	@UiField Button reindex;
	
	CrudServiceAsync service = GWT.create(CrudService.class);

	private CrudEntityInfo info;

	private int times;

	private final SelectionList<Boolean> typeSelector;
	
	interface ReindexEntityPanelUiBinder extends
			UiBinder<Widget, ReindexEntityPanel> {
	}

	public ReindexEntityPanel(CrudEntityInfo info, SelectionList<Boolean> typeSelector) {
		this.info = info;
		this.typeSelector = typeSelector;
		initWidget(uiBinder.createAndBindUi(this));
		String underscore = info.getEntityName().replace('.', '_');
		name.setInnerText(CrudLabelHelper.getString(underscore));
		progress.getStyle().setVisibility(Visibility.HIDDEN);
	}

	
	@UiHandler("reindex")
	public void reindexClicked(ClickEvent e) {
		reindex(typeSelector.getValue(), new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				//Do nothing
			}
			
			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}
		});
	}

	/**
	 * Starts a process to reindex the entity
	 * @param full 
	 * 
	 * @param asyncCallback the callback used when done.
	 */
	public void reindex(Boolean full, final AsyncCallback<Void> callback) {
		setProgress(0);
		progressText.setInnerText(messages.initializing());
		reindex.setEnabled(false);
		
		if (full) {
			service.count(info.getEntityName(), new AsyncCallback<Integer>() {
				@Override
				public void onSuccess(Integer count) {
					initIndexing(count.intValue(), callback);
				}
				@Override
				public void onFailure(Throwable caught) {
					failed(callback, caught);
				}
			});
		} else {
			progressText.setInnerText(messages.partialReindex());
			service.reindexPartial(info.getEntityName(), new AsyncCallback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					done(callback);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					failed(callback, caught);
				}
			});
		}
		
	}


	protected void initIndexing(int count, final AsyncCallback<Void> callback) {
		times = (int) Math.ceil((double)count / BATCH_SIZE);	
		progressText.setInnerText(messages.clearingIndex());
		
		recursiveClearIndex(callback);
	}

	private void recursiveClearIndex(final AsyncCallback<Void> callback) {
		
		service.clearIndexForEntity(info.getEntityName(), new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if (result.booleanValue()) {
					recursiveIndexBatch(0, callback);
				} else {
					recursiveClearIndex(callback);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				failed(callback, caught);
			}
		});

	}


	private void done(AsyncCallback<Void> callback) {
		setProgress(1.0d);
		callback.onSuccess(null);
		progressText.setInnerText(messages.done());
	}

	private void failed(AsyncCallback<Void> callback, Throwable caught) {
		progressText.setInnerText(messages.failed());
		callback.onFailure(caught);
	}

	

	private void recursiveIndexBatch(final int index, final AsyncCallback<Void> callback) {
		if (index == times) {
			done(callback);
			return;
		}
		setProgress((double)index/(double)times);
		service.reindex(info.getEntityName(), new PageMetaData<String>(BATCH_SIZE, index, null, false), new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void voi) {
				recursiveIndexBatch(index+1, callback);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				progressText.setInnerText("Retrying...");
				recursiveIndexBatch(index, callback);
			}
		});
	}


	private void setProgress(double zeroToOne) {
		progress.getStyle().setVisibility(Visibility.VISIBLE);
		long percent = Math.round(zeroToOne * 100d);
		progressInner.getStyle().setWidth(percent, Unit.PCT);
		progressText.setInnerText(percent + "%");
	}
	
	
}

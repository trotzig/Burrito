package burrito.client.reindex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.generic.CrudEntityInfo;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ReindexPanel extends Composite {

	private static ReindexPanelUiBinder uiBinder = GWT
			.create(ReindexPanelUiBinder.class);
	private static CrudMessages messages = GWT.create(CrudMessages.class);
	
	@UiField HTMLPanel entities; 
	@UiField Button reindexAll;
	@UiField SimplePanel typeWrapper;
	private List<CrudEntityInfo> allEntities;
	private List<ReindexEntityPanel> panels = new ArrayList<ReindexEntityPanel>();
	private SelectionList<Boolean> type;
	
	interface ReindexPanelUiBinder extends UiBinder<Widget, ReindexPanel> {
	}

	public ReindexPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		entities.add(new Label(messages.loading()));
		CrudServiceAsync service = GWT.create(CrudService.class);
		service.getAllEntities(new AsyncCallback<List<CrudEntityInfo>>() {
			
			@Override
			public void onSuccess(List<CrudEntityInfo> result) {
				allEntities = result;
				init();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}
		});
		type = new SelectionList<Boolean>(false);
		type.setLabelCreator(new SelectionListLabelCreator<Boolean>() {
			
			@Override
			public String createLabel(Boolean full) {
				if (full) {
					return messages.reindexTypeFull();
				}
				return messages.reindexTypePartial();
			}
		});
		type.setModel(Arrays.asList(false, true));
		type.render();
		typeWrapper.add(type);
	}
	
	protected void init() {
		entities.clear();
		for (CrudEntityInfo info : allEntities) {
			ReindexEntityPanel panel = new ReindexEntityPanel(info, type);
			entities.add(panel);
			panels .add(panel);
		}
	}

	@UiHandler("reindexAll")
	public void onReindexAll(ClickEvent e) {
		reindexAllIter(0);
		reindexAll.setEnabled(false);
	}

	private void reindexAllIter(final int i) {
		if (i == panels.size()) {
			return;
		}
		panels.get(i).reindex(type.getValue(), new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				reindexAllIter(i+1);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				reindexAllIter(i+1); //never mind errors, just continue with the next.
			}
		});
	}

}

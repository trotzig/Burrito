package burrito.client.crud.input;

import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.FileField;
import burrito.client.dto.BlobInfoDTO;
import burrito.client.widgets.blobstore.BlobStoreUploader;
import burrito.client.widgets.messages.CommonMessages;
import burrito.client.widgets.services.BlobService;
import burrito.client.widgets.services.BlobServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("rawtypes")
public class FileCrudInputField implements CrudInputField {

	private final FileField field;
	protected String blobStoreKey;
	
	private HorizontalPanel panel;
	private BlobStoreUploader blobField;
	private HorizontalPanel fileInfoPanel = new HorizontalPanel();
	private Button changeFile = new Button("Change file"); //move to message

	private BlobServiceAsync service = GWT.create(BlobService.class);
	private CommonMessages messages = GWT.create(CommonMessages.class);
	
	public FileCrudInputField(FileField field) {
		this.field = field;
		
		panel = new HorizontalPanel();
		
		blobField = new BlobStoreUploader(new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				blobStoreKey = result;
				renderFileInfo();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException("Failed to upload file");
			}
		});
		
		changeFile.setVisible(false);
		fileInfoPanel.setVisible(false);
		
		fileInfoPanel.setStyleName("k5-FileCrudInputField-fileInfo");
		
		panel.add(fileInfoPanel);
		panel.add(changeFile);
		panel.add(blobField);
		
		changeFile.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeFile.setVisible(false);
				blobField.setVisible(true);
			}
		});
		
		load(field.getValue());
	}

	@Override
	public Widget getDisplayWidget() {
		return panel;
	}

	@Override
	public void load(Object value) {
		blobStoreKey = (String) value;
		renderFileInfo();
	}
	
	private void renderFileInfo() {
		service.getFileInfo(blobStoreKey, new AsyncCallback<BlobInfoDTO>() {
			
			@Override
			public void onSuccess(BlobInfoDTO result) {
				if (result == null) {
					changeFile.setVisible(false);
					fileInfoPanel.setVisible(false);
				} else {
					changeFile.setVisible(true);
					fileInfoPanel.setVisible(true);
					blobField.setVisible(false);
					
					fileInfoPanel.clear();
					fileInfoPanel.add(new Label(result.getFilename()));
					fileInfoPanel.add(new Label(result.getContentType()));
					fileInfoPanel.add(new Label(result.getCreation().toString()));
					fileInfoPanel.add(new Anchor("Download", "/blobstore/serve?key=" + blobStoreKey));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException("Could not get fileinfo.");
			}
		});
	}

	@Override
	public Object getValue() {
		return blobStoreKey;
	}

	@Override
	public CrudField getCrudField() {
		field.setValue(blobStoreKey);
		return field;
	}

}
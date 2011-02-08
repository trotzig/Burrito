package burrito.client.widgets.blobstore;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import burrito.client.widgets.services.BlobService;
import burrito.client.widgets.services.BlobServiceAsync;

public class BlobStoreUploader extends Composite {
	private FormPanel form = new FormPanel();
	private BlobServiceAsync service = GWT.create(BlobService.class);
	private FileUpload file = new FileUpload();
	private Image progress = new Image("/themes/default/images/loading.gif");
	
	public BlobStoreUploader(final AsyncCallback<String> callback) {
		this(null, null, callback);
	}

	public BlobStoreUploader(Integer requiredWidth, Integer requiredHeight, final AsyncCallback<String> callback) {
		FlowPanel inner = new FlowPanel();
		inner.add(file);
		file.setName("file");
		file.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				getUploadURLAndPost();
			}
		});
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		if (requiredWidth != null) {
			inner.add(new Hidden("width", String.valueOf(requiredWidth)));
		}
		if (requiredHeight != null) {
			inner.add(new Hidden("height", String.valueOf(requiredHeight)));
		}
		form.addSubmitHandler(new SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				file.setVisible(false);
				progress.setVisible(true);
			}
		});
		
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				file.setVisible(true);
				progress.setVisible(false);
				String results = event.getResults().replaceAll("<.*?>", ""); // sometime, <pre> tags are added by the browser
				if (results.contains("OK#")) {
					String blobStoreKey = results.replace("OK#", "");
					callback.onSuccess(blobStoreKey);
				} else {
					Window.alert(results);
					callback.onFailure(new RuntimeException("Failure response from server"));
				}
			}
		});
		progress.setVisible(false);
		inner.add(progress);
		
		form.add(inner);
		initWidget(form);
	}

	protected void getUploadURLAndPost() {
		service.getBlobStoreUploadURL(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String uploadURL) {
				doPost(uploadURL);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException("Failed to get upload URL", caught);
			}
		});
	}

	protected void doPost(String uploadURL) {
		form.setAction(uploadURL);
		form.submit();
	}
}

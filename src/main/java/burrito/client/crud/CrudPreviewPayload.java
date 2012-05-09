package burrito.client.crud;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CrudPreviewPayload implements Serializable {

	private String previewData;
	private String previewUrl;

	public CrudPreviewPayload() {
	}

	public CrudPreviewPayload(String previewData, String previewUrl) {
		this.previewData = previewData;
		this.previewUrl = previewUrl;
	}

	public String getPreviewData() {
		return previewData;
	}

	public void setPreviewData(String previewData) {
		this.previewData = previewData;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
}

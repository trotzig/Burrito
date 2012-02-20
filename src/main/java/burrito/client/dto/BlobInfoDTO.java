package burrito.client.dto;

import java.io.Serializable;
import java.util.Date;

public class BlobInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String filename;
	private String contentType;
	private long size;
	private Date creation;
	private String blobKey;

	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}
	
	public Date getCreation() {
		return creation;
	}
	
	public void setCreation(Date creation) {
		this.creation = creation;
	}
	
	public String getBlobKey() {
		return blobKey;
	}
	
	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}
}

package burrito.services;

import java.io.Serializable;

import siena.Model;

@SuppressWarnings("serial")
public class SearchEntry implements Serializable {

	private Long ownerId;
	private Class<? extends Model> ownerClass;
	private String title;
	/**
	 * A snippet of HTML highlighting the search hit
	 */
	private String snippetHtml;
	
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Class<? extends Model> getOwnerClass() {
		return ownerClass;
	}
	public void setOwnerClass(Class<? extends Model> ownerClass) {
		this.ownerClass = ownerClass;
	}
	public String getSnippetHtml() {
		return snippetHtml;
	}
	public void setSnippetHtml(String snippetHtml) {
		this.snippetHtml = snippetHtml;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}

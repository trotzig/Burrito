package burrito.services;

import java.io.Serializable;
/**
 * A class representing search hits, usually returned from a {@link SearchManager}
 * 
 * @author henper
 *
 */
@SuppressWarnings("serial")
public class SearchHit implements Serializable {

	private Long ownerId;
	private String ownerClass;
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
	
	public String getOwnerClass() {
		return ownerClass;
	}
	public void setOwnerClass(String ownerClass) {
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

package burrito.links;

/**
 * Simple representation of a link
 * 
 * @author henper
 * 
 */
public class Link {

	private String url;
	private String text;
	
	public Link() {
		//default constructor
	}
	
	public Link(String url, String text) {
		this.url = url;
		this.text = text;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	

}

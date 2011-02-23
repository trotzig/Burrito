package burrito.sitelet;

public class SiteletBoxMemberMessage {

	private Long id;
	private String html;

	public SiteletBoxMemberMessage(Long id, String html) {
		this.id = id;
		this.html = html;
	}

	public SiteletBoxMemberMessage(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

}

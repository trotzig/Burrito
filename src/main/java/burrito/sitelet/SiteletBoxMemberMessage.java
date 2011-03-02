package burrito.sitelet;

public class SiteletBoxMemberMessage {

	private Long id;
	private Integer version;
	private String html;

	public SiteletBoxMemberMessage(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
}

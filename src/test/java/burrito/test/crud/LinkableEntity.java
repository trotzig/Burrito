package burrito.test.crud;

import burrito.links.Linkable;
import siena.Generator;
import siena.Id;
import siena.Model;

public class LinkableEntity extends Model implements Linkable {

	@Id(Generator.AUTO_INCREMENT)
	private Long id;

	private String url;

	public Long getId() {
		return id;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

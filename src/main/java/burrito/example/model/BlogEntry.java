package burrito.example.model;

import java.util.Date;

import burrito.annotations.Displayable;
import burrito.annotations.ReadOnly;
import burrito.annotations.Required;
import burrito.annotations.RichText;
import burrito.annotations.SearchableField;
import burrito.links.Linkable;
import burrito.util.StringUtils;

import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;

public class BlogEntry extends Model implements Linkable {

	@Id(Generator.AUTO_INCREMENT)
	private Long id;

	@SearchableField
	@Displayable
	@Required
	private String heading;
	
	@ReadOnly
	private Date created;
	
	@SearchableField
	@Required
	@RichText
	private String content;

	@Override
	public String getUrl() {
		return "/entry/" + StringUtils.makeSEOFriendly(heading) + "/" + id;
	}
	
	public static Query<BlogEntry> all() {
		return Model.all(BlogEntry.class);
	}
	
	public String getQuote() {
		return StringUtils.cut(StringUtils.stripHTML(content), 100);
	}
	
	@Override
	public void insert() {
		this.created = new Date();
		super.insert();
	}

	public Long getId() {
		return id;
	}

	public String getHeading() {
		return heading;
	}

	public Date getCreated() {
		return created;
	}

	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		return heading;
	}
	
		
}

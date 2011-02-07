package burrito.example.model;

import java.util.List;

import burrito.annotations.Required;
import burrito.sitelet.Sitelet;

import siena.Generator;
import siena.Id;
import siena.Model;

public class BlogQuotes extends Model implements Sitelet {
	@Id(Generator.AUTO_INCREMENT)
	private Long id;

	@Required
	private Integer max = 5;

	public List<BlogEntry> getEntries() {
		return BlogEntry.all().order("-created").fetch(max);
	}

	public Long getId() {
		return id;
	}
}

package burrito.example;

import burrito.example.model.BlogEntry;
import mvcaur.Controller;

public class BlogEntryController implements Controller<BlogEntry> {

	private Long id;
	
	@Override
	public BlogEntry execute() {
		return BlogEntry.all().filter("id", id).get();
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}

}

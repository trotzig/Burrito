package burrito.example;

import java.util.List;

import burrito.example.model.BlogEntry;


import mvcaur.Controller;

public class StartPageController implements Controller<List<BlogEntry>> {

	@Override
	public List<BlogEntry> execute() {
		return BlogEntry.all().order("-created").fetch();
	}

}

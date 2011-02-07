package burrito.example;

import burrito.example.model.BlogEntry;
import burrito.example.model.BlogQuotes;

public class Configurator extends burrito.Configurator {

	@Override
	protected void init() {
		addCrudable(BlogEntry.class);
		addLinkable(BlogEntry.class);
		addSitelet(BlogQuotes.class);
	}
	
}

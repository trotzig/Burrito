package burrito.controller;

import taco.Controller;
import burrito.util.BBCodeCreator;
import burrito.util.Cache;

public class BBPreviewController implements Controller<String> {

	private String key;
	
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String execute() {
		String bbcode = (String) Cache.get(key);
		String body = BBCodeCreator.generateHTML(bbcode);
		return body;
	}

}

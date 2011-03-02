package burrito.controller;

import taco.Controller;
import burrito.services.SiteletProperties;
import burrito.sitelet.SiteletBoxFeedMessage;

public class PollSiteletBoxController implements Controller<SiteletBoxFeedMessage> {

	private String containerId;

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	@Override
	public SiteletBoxFeedMessage execute() {
		return SiteletProperties.getSiteletBoxFeedMessage(containerId, null, true);
	}
}

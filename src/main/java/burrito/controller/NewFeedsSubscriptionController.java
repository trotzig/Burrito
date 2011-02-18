package burrito.controller;

import java.util.HashMap;
import java.util.Map;

import taco.Controller;
import burrito.services.FeedsSubscription;

public class NewFeedsSubscriptionController implements Controller<Map<String, String>> {

	private String method;
	
	
	@Override
	public Map<String, String> execute() {
		Map<String, String> result = new HashMap<String, String>();

		FeedsSubscription subscription = new FeedsSubscription();
		if (!"poll".equals(method)) {
			//don't request a channel if polling is requested
			subscription.requestChannel();
		}
		subscription.insert();

		result.put("status", "ok");
		result.put("subscriptionId", subscription.getId().toString());

		String channelId = subscription.getChannelId();
		if (channelId != null) result.put("channelId", channelId);

		return result;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}
	
	
}

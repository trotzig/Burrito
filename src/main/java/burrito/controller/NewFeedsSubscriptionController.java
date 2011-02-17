package burrito.controller;

import java.util.HashMap;
import java.util.Map;

import taco.Controller;
import burrito.services.FeedsSubscription;

public class NewFeedsSubscriptionController implements Controller<Map<String, String>> {

	@Override
	public Map<String, String> execute() {
		Map<String, String> result = new HashMap<String, String>();

		FeedsSubscription subscription = new FeedsSubscription();
		subscription.requestChannel();
		subscription.insert();

		result.put("status", "ok");
		result.put("subscriptionId", subscription.getId().toString());

		String channelId = subscription.getChannelId();
		if (channelId != null) result.put("channelId", channelId);

		return result;
	}
}

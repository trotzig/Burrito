package burrito.controller;

import java.util.HashMap;
import java.util.Map;

import burrito.services.FeedsSubscription;

import taco.Controller;

public class NewFeedsSubscriptionChannelController implements Controller<Map<String, String>> {

	private Long subscriptionId;

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	@Override
	public Map<String, String> execute() {
		Map<String, String> result = new HashMap<String, String>();

		FeedsSubscription subscription = FeedsSubscription.getById(subscriptionId);

		if (subscription != null)
		{
			subscription.requestChannel();
			subscription.update();

			result.put("status", "ok");

			String channelId = subscription.getChannelId();
			if (channelId != null) result.put("channelId", channelId);
		}
		else
		{
			result.put("status", "error");
			result.put("message", "No such subscription");
		}

		return result;
	}
}

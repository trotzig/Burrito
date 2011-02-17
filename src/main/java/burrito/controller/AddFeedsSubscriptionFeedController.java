package burrito.controller;

import java.util.HashMap;
import java.util.Map;

import burrito.services.FeedsSubscription;

import taco.Controller;

public class AddFeedsSubscriptionFeedController implements Controller<Map<String, String>> {

	private Long subscriptionId;
	private String feedId;

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	@Override
	public Map<String, String> execute() {
		Map<String, String> result = new HashMap<String, String>();

		FeedsSubscription subscription = FeedsSubscription.getById(subscriptionId);

		if (subscription != null)
		{
			if (!subscription.hasFeedId(feedId))
			{
				subscription.addFeedId(feedId);
				subscription.update();
			}

			result.put("status", "ok");
		}
		else
		{
			result.put("status", "error");
			result.put("message", "No such subscription");
		}

		return result;
	}
}

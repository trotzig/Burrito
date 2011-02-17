package burrito.controller;

import java.util.HashMap;
import java.util.Map;

import burrito.services.ChannelSubscription;

import taco.Controller;

public class AddChannelFeedController implements Controller<Map<String, String>> {

	private String channelId;
	private String feedId;

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	@Override
	public Map<String, String> execute() {
		Map<String, String> result = new HashMap<String, String>();

		ChannelSubscription subscription = ChannelSubscription.getByChannelId(channelId);

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
			result.put("message", "No such channel subscription");
		}

		return result;
	}
}

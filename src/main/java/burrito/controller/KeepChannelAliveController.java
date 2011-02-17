package burrito.controller;

import java.util.HashMap;
import java.util.Map;

import burrito.services.ChannelSubscription;

import taco.Controller;

public class KeepChannelAliveController implements Controller<Map<String, String>> {

	private String channelId;

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public Map<String, String> execute() {
		Map<String, String> result = new HashMap<String, String>();

		ChannelSubscription subscription = ChannelSubscription.getByChannelId(channelId);

		if (subscription != null)
		{
			subscription.keepAlive();
			subscription.update();

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

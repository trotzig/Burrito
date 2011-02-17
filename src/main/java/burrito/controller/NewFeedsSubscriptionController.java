package burrito.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import burrito.services.FeedsSubscription;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

import taco.Controller;

public class NewFeedsSubscriptionController implements Controller<Map<String, String>> {

	@Override
	public Map<String, String> execute() {
		Map<String, String> result = new HashMap<String, String>();

		String clientId = UUID.randomUUID().toString();

		ChannelService service = ChannelServiceFactory.getChannelService();
		String channelId = service.createChannel(clientId);

		FeedsSubscription subscription = new FeedsSubscription();
		subscription.setClientId(clientId);
		subscription.setChannelId(channelId);
		subscription.insert();

		result.put("status", "ok");
		result.put("subscriptionId", subscription.getId().toString());
		result.put("method", "push");
		result.put("channelId", channelId);

		return result;
	}
}

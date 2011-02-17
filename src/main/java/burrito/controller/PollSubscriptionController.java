package burrito.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taco.Controller;
import burrito.services.FeedsSubscriptionMessage;

public class PollSubscriptionController implements Controller<Map<String, Object>> {

	private Long subscriptionId;

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	@Override
	public Map<String, Object> execute() {
		Map<String, Object> result = new HashMap<String, Object>();

		List<FeedsSubscriptionMessage> messages = FeedsSubscriptionMessage.fetchBySubscriptionId(subscriptionId);
		List<Map<String, String>> resultMessages = new ArrayList<Map<String, String>>(messages.size());

		for (FeedsSubscriptionMessage message : messages)
		{
			Map<String, String> resultMessage = new HashMap<String, String>();
			resultMessage.put("feedId", message.getFeedId());
			resultMessage.put("message", message.getMessage());
			resultMessages.add(resultMessage);

			message.delete();
		}

		result.put("status", "ok");
		result.put("messages", resultMessages);

		return result;
	}
}

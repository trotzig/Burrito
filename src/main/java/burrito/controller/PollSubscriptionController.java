/**
 * Copyright 2011 Henric Persson (henric.persson@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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

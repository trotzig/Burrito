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

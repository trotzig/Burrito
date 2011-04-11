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

import taco.Controller;
import burrito.services.FeedsSubscription;

public class NewFeedsSubscriptionController implements Controller<Map<String, String>> {

	private String method;
	private String channelId;

	@Override
	public Map<String, String> execute() {
		Map<String, String> result = new HashMap<String, String>();

		FeedsSubscription subscription = null;

		if ("push".equals(method) && channelId != null) {
			subscription = FeedsSubscription.getByChannelId(channelId);
			if (subscription != null) {
				subscription.reuse();
				subscription.update();
			}
		}

		if (subscription == null) {
			subscription = new FeedsSubscription();
			if ("push".equals(method)) {
				//don't request a channel if polling is requested
				subscription.requestChannel();
			}
			subscription.insert();
		}

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

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
}

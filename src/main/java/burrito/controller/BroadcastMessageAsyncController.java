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

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.HashMap;
import java.util.Map;

import burrito.services.DeferredMessage;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.repackaged.com.google.common.base.CharEscapers;

/**
 * Adds a task to the queue handled by {@link BroadcastMessageController}
 * 
 * @author henper
 * 
 */
public class BroadcastMessageAsyncController extends BroadcastMessageController {

	@Override
	public Map<String, String> execute() {
		// Add a task to the queue
		Queue queue = QueueFactory.getQueue("burrito-broadcast");
		TaskOptions options = withUrl("/burrito/feeds/"
				+ CharEscapers.uriEscaper(false).escape(getFeedId())
				+ "/broadcast");
		if (getMessage().length() > 1500) {
			// message is too large to add directly to task queue. Store it in
			// the datastore and send only the id.
			// 1500 characters is actually a lot lower than what the task queue
			// can handle. But we need to use a fairly safe value.
			// A value of 2000 should be ok as well but to be on the safe side
			// we set the limit lower.
			DeferredMessage m = new DeferredMessage();
			m.setMessage(getMessage());
			m.insert();
			options.param("deferredMessageId", String.valueOf(m.getId()));
		} else {
			options.param("message", getMessage());
		}
		options.param("feedId", getFeedId());
		options.param("secret", getSecret());
		if (getExcludeSubscriptionId() != null) {
			options.param("excludeSubscriptionId",
					String.valueOf(getExcludeSubscriptionId()));
		}
		queue.add(options);
		Map<String, String> result = new HashMap<String, String>();
		result.put("status", "ok");
		return result;
	}

}

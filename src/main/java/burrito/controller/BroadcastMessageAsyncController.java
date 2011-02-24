package burrito.controller;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.HashMap;
import java.util.Map;

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
public class BroadcastMessageAsyncController extends
		BroadcastMessageController {
	
	@Override
	public Map<String, String> execute() {
		validateSecret();
		// Add a task to the queue
		Queue queue = QueueFactory.getDefaultQueue();
		TaskOptions options = withUrl("/burrito/feeds/" + CharEscapers.uriEscaper(false).escape(getFeedId()) + "/broadcast").param("message",
				getMessage()).param("feedId", getFeedId()).param("secret", getSecret());
		if (getExcludeSubscriptionId() != null) {
			options.param("excludeSubscriptionId", String.valueOf(getExcludeSubscriptionId()));
		}
		queue.add(options);
		Map<String, String> result = new HashMap<String, String>();
		result.put("status", "ok");
		return result;
	}

}

package burrito.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import taco.Controller;
import burrito.services.DeferredMessage;
import burrito.services.FeedsSubscription;
import burrito.services.FeedsSubscriptionMessage;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gson.Gson;

public class BroadcastMessageController implements Controller<Map<String, String>> {

	private String feedId;
	private String message;
	private Long excludeSubscriptionId;
	private String secret;
	private Long deferredMessageId;
	
	private static final Logger log = Logger.getLogger(BroadcastMessageController.class.getName());
	
	
	@Override
	public Map<String, String> execute() {
		DeferredMessage deferredMessage = null;
		if (message == null) {
			//we need to fetch the deferred message from the datastore
			deferredMessage = DeferredMessage.get(deferredMessageId);
			message = deferredMessage.getMessage();
		}
		log("Starting broadcast to feed \""+feedId+"\". Message: \n" + message);
		ChannelService channelService = ChannelServiceFactory
				.getChannelService();
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", message);
		map.put("feedId", feedId);
		String wrapped = new Gson().toJson(map);
		// get a list of people (browser clients) to notify
		Iterable<FeedsSubscription> subs = FeedsSubscription.getSubscriptionsForFeed(feedId);
		int nOfPush = 0;
		int nOfPoll = 0;
		int nOfFailed = 0;
		for (FeedsSubscription sub : subs) {
			if (excludeSubscriptionId != null
					&& excludeSubscriptionId.equals(sub.getId())) {
				//don't push to this channel
				continue;
			}
			if (sub.getChannelId() == null) {
				//polling, store the update until next poll
				FeedsSubscriptionMessage msg = new FeedsSubscriptionMessage();
				msg.setFeedId(feedId);
				msg.setMessage(message);
				msg.setSubscriptionId(sub.getId());
				msg.insert();
				nOfPoll++;
			} else {
				try {
					// Push to client
					channelService.sendMessage(new ChannelMessage(sub
							.getClientId(), wrapped));
					nOfPush++;
				} catch (Exception e) {
					System.err.println("Failed: "
							+ sub.toString()
							+ ". Exception: " + e.getMessage());
					nOfFailed++;
				}
			}
		}
		if (deferredMessage != null) {
			//cleanup the deferred message
			try {
				deferredMessage.delete();
			} catch (Exception e) {
				// ignore failed deletes
				log.warning("Failed to remove deferred message from datastore. The message will still be broadcasted but you will have to manually remove the message from the DeferredMessage table");
			}
		}
		
		log("Successful push. Number of push channels: " + nOfPush + 
				". Number of poll channels: " + nOfPoll + 
				". Number of failed: " + nOfFailed);
		Map<String, String> result = new HashMap<String, String>();
		result.put("status", "ok");
		result.put("numberOfPush", String.valueOf(nOfPush));
		result.put("numberOfPoll", String.valueOf(nOfPoll));
		result.put("numberOfFailed", String.valueOf(nOfFailed));
		return result;
		
		
	}

	private void log(String string) {
		log.log(Level.INFO, string);
	}


	public String getFeedId() {
		return feedId;
	}
	
	public void setDeferredMessageId(Long deferredMessageId) {
		this.deferredMessageId = deferredMessageId;
	}
	
	public Long getDeferredMessageId() {
		return deferredMessageId;
	}


	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}



	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Long getExcludeSubscriptionId() {
		return excludeSubscriptionId;
	}


	public void setExcludeSubscriptionId(Long excludeSubscriptionId) {
		this.excludeSubscriptionId = excludeSubscriptionId;
	}


	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	



	
}

package burrito.controller;

import java.util.Map;

import taco.Controller;

public class BroadcastMessageController implements Controller<Map<String, String>> {

	private String feedId;
	private String data;
	
	
	@Override
	public Map<String, String> execute() {
		return null;
//		Date startTime = new Date();
//
//		// The authorChannelId parameter may very well be null in cases where
//		// the comment was made from someone who was using the poll method
//		// (instead of push)
//		String authorChannelId = req.getParameter("authorChannelId");
//
//		// Fetch the comment that was posted
//		String jsonUpdates = getJson(comment.getThreadId(),
//				comment.getSiteId(), comment);
//		log("Message to send: " + jsonUpdates, resp);
//
//		ChannelService channelService = ChannelServiceFactory
//				.getChannelService();
//
//		// get a list of people (browser clients) to notify
//		List<ThreadSubscription> subs = ThreadSubscription.getSubscriptions(
//				comment.getSiteId(), comment.getThreadId());
//		int success = 0;
//		int failed = 0;
//		for (ThreadSubscription sub : subs) {
//			if (authorChannelId != null
//					&& authorChannelId.equals(sub.getChannelId())) {
//				// Author gets its update directly from the callback to post.js.
//				// No need to notify him.
//				continue;
//			}
//			try {
//				// Push to client
//				channelService.sendMessage(new ChannelMessage(sub
//						.getGeneratedClientId(), jsonUpdates));
//				log("Success: " + sub.toString(), resp);
//				success++;
//			} catch (Exception e) {
//				log(Level.ERROR, "Failed: "
//						+ sub.toString()
//						+ ". Exception: " + e.getMessage(), resp);
//				failed++;
//			}
//		}
//		Date endTime = new Date();
//		log("Finished pushing " + endTime, resp);
//		long diff = endTime.getTime() - startTime.getTime();
//		double diffS = (double)diff/1000.0d;
//		log("Total push time: " + diffS + " seconds", resp);
//		log("Total number of recipients: " + subs.size(), resp);
//		log("Total number of successful pushes: " + success, resp);
//		log("Total number of successful pushes: " + failed, resp);
//		
	}


	public String getFeedId() {
		return feedId;
	}


	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}

	
	
	
}

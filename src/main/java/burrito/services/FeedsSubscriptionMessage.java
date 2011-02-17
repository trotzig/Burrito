package burrito.services;

import java.util.List;

import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;

public class FeedsSubscriptionMessage extends Model {

	@Id(Generator.AUTO_INCREMENT)
	private Long id;

	private Long subscriptionId;

	private String feedId;

	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getFeedId() {
		return feedId;
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

	private static Query<FeedsSubscriptionMessage> all() {
		return Model.all(FeedsSubscriptionMessage.class);
	}

	public static List<FeedsSubscriptionMessage> fetchBySubscriptionId(Long subscriptionId) {
		return all().filter("subscriptionId", subscriptionId).fetch();
	}
}

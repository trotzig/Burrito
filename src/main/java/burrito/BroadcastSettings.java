package burrito;

/**
 * An object describing settings for broadcasting messages
 * 
 * @author henper
 * 
 */
public class BroadcastSettings {

	private String feedsDomain;
	private String secret;

	/**
	 * Creates a new {@link BroadcastSettings} object with an external feeds
	 * server
	 * 
	 * @param feedsDomain
	 *            the domain where the feeds server is reachable, e.g.
	 *            myfeeds.appspot.com. Don't include the http/https protocol
	 *            here.
	 * @param secret
	 *            a secret string shared by the feeds server and this
	 *            application
	 */
	public BroadcastSettings(String feedsDomain, String secret) {
		this.feedsDomain = feedsDomain;
		this.secret = secret;
	}

	/**
	 * Creates a new {@link BroadcastSettings} object configured to run within
	 * the same site.
	 * 
	 * @param secret
	 *            a secret string used to autorize broadcast messages.
	 */
	public BroadcastSettings(String secret) {
		this.secret = secret;
	}

	/**
	 * Gets the secret
	 * 
	 * @return
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * Gets a URL prefix to the location where you should subscribe to feeds.
	 * Includes the http:// part
	 * 
	 * @return
	 */
	public String getSubscribeUrlPrefix() {
		if (feedsDomain == null) {
			return "";
		}
		return "http://" + feedsDomain;
	}

	/**
	 * Gets a URL prefix to where to broadcast messages
	 * 
	 * @return
	 */
	public String getBroadcastUrlPrefix() {
		if (feedsDomain == null) {
			return "";
		}
		return "https://" + feedsDomain;
	}

	public boolean isBroadcastInternally() {
		return feedsDomain == null;
	}

}

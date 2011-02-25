/**
 * Channel implementation using polling
 * 
 * @param subscriptionId
 * @param feedServer
 * @returns {BurritoPollingChannel}
 */
function BurritoPollingChannel(subscriptionId, feedServer) {
	var object = this;

	this.pollingIntervalSeconds = 10;

	this.subscriptionId = subscriptionId;
	this.currentTimeout = -1;
	this.feedServer = feedServer;
	
	this.onmessage = function(json){}; //callback for messages
	
	this.open = function() {
		this.triggerNewPoll();
		return this;
	}
	
	this.onMessages = function(json) {
		if (!json.messages) {
			return;
		}
		//loop through all messages received and call the onmessage callback
		for (var i = 0; i < json.messages.length; i++) {
			var msgJson = json.messages[i];
			this.onmessage(msgJson);
		}
	}
	
	this.doPoll = function() {
		$.ajax({
			url: this.feedServer + '/burrito/feeds/subscription/'+this.subscriptionId+'/poll', 
			dataType: "jsonp",
			crossDomain: true,
			success: function(json) {
				if (json.status == 'error') {
					throw("Error response from feed server: " + json.message);
				}
				object.onMessages(json);
			}
		});
	}
	
	this.triggerNewPoll = function() {
		this.currentTimeout = setTimeout(function() {
			object.doPoll();
			object.triggerNewPoll();
		}, this.pollingIntervalSeconds * 1000);
	}
}

/**
 * Burrito Feeds Object
 * 
 * Responsible for keeping a channel open to the feed server. 
 * 
 * @returns
 */
function BurritoFeeds() {
	var object = this;
	
	this.pendingHandlers = new Array();
	this.registeredHandlers = new Array();
	
	this.feedServer = ''; //defaults to same domain as website
	this.method = 'push';
	this.channelId = '';
	this.subscriptionId = -1;
	this.subscriptionRequestSent = false;

	/**
	 * Registers a new feed handler
	 */
	this.registerHandler = function(feedId, callback) {
		if (this.subscriptionId < 0) {
			this.pendingHandlers[feedId] = callback;

			if (!this.subscriptionRequestSent) {	
				this.subscriptionRequestSent = true;
				this.getSubscriptionAndOpenChannel();
			}
		}
		else {
			this.registeredHandlers[feedId] = callback;
			this.addFeedToSubscription(feedId);
		}
	}

	this.addFeedToSubscription = function(feedId) {
		if (this.addFeedToSubscriptionLock) {
			setTimeout(function() {
				this.addFeedToSubscription(feedId);
			}, 200);
		}
		else {
			this.addFeedToSubscriptionLock = true;

			$.ajax({
				url: this.feedServer + '/burrito/feeds/subscription/' + this.subscriptionId + '/addFeed/' + encodeURIComponent(feedId),
				crossDomain: true,
				dataType: "jsonp",
				success: function(json) {
					this.addFeedToSubscriptionLock = false;
					if (json.status == 'error') {
						throw("Error response from feed server: " + json.message);
					}
				}
			});
		}
	}

	/**
	 * Overrides the default channel server
	 */
	this.setFeedServer = function(chServer) {
		this.feedServer = chServer;
	}
	
	/**
	 * Sets the method used for subscribing to feeds. Either 'poll' or 'push'. 
	 * The feed server will try to respect your setting, but it is not guaranteed. 
	 */
	this.setMethod = function(mthd) {
		this.method = mthd;
	}
	
	this.getSubscriptionAndOpenChannel = function() {
		$.ajax({
			url: this.feedServer + '/burrito/feeds/subscription/new/' + encodeURIComponent(this.method), 
			dataType: "jsonp",
			crossDomain: true,
			success: function(json) {
				if (json.status == 'error') {
					throw("Error response from feed server: " + json.message);
				}

				object.subscriptionId = json.subscriptionId;
				object.triggerNewKeepAlive();

				//iterate through all non-initialized handlers and make sure they are initialized
				for (var feedId in object.pendingHandlers) {
					object.registerHandler(feedId, object.pendingHandlers[feedId]);
					object.pendingHandlers[feedId] = null;
				}

				object.startListeningToChannel(json.channelId);
			}
		});
	}

	this.getNewChannel = function() {
		$.ajax({
			url: this.feedServer + '/burrito/feeds/subscription/' + this.subscriptionId + '/newChannel', 
			dataType: "jsonp",
			crossDomain: true,
			success: function(json) {
				if (json.status == 'error') {
					throw("Error response from feed server: " + json.message);
				}
				object.startListeningToChannel(json.channelId);
			}
		});
	}

	this.dropChannel = function() {
		$.ajax({
			url: this.feedServer + '/burrito/feeds/subscription/' + this.subscriptionId + '/dropChannel', 
			dataType: "jsonp",
			crossDomain: true,
			success: function(json) {
				if (json.status == 'error') {
					throw("Error response from feed server: " + json.message);
				}
				object.startListeningToChannel(null);
			}
		});
	}

	this.startListeningToChannel = function(channelId) {
		this.channelId = channelId;
		if (channelId) {
			this.openGoogleChannel();
		} else {
			this.openPollingChannel();
		}
	}

	this.onMessageReceived = function(json) {
		var targetFeedId = json.feedId;
	 	for (var feedId in this.registeredHandlers) {
	 		if (feedId == targetFeedId) {
	 			var callback = this.registeredHandlers[feedId];
	 			callback(json.message);
	 		}
		}
	}
	
	this.openPollingChannel = function() {
		var pollingChannel = new BurritoPollingChannel(this.subscriptionId, this.feedServer);
		var socket = pollingChannel.open();
		socket.onmessage = function(json) {
			object.onMessageReceived(json);
		}
	}

	this.openGoogleChannel = function() {
		var lastChannelRetryTime = 0;
		var channel = new goog.appengine.Channel(this.channelId);
		var socket = channel.open();

		socket.onopen = function() {
			//do something?
		}

		socket.onclose = function(evt) {
			if ((new Date().getTime()) - lastChannelRetryTime < 5 * 60 * 1000) {
				object.dropChannel(); // closes too frequently, so we give up
			}
			else {
				lastChannelRetryTime = new Date().getTime();
				object.getNewChannel(); // attempt to recreate channel
			}
		}

		socket.onmessage = function(evt) {
		 	var json = eval("(" + evt.data + ")");
		 	object.onMessageReceived(json);
		} 

		socket.onerror = function(error) {
			//do something!?
		} 
	}
	
	this.keepAlive = function() {
		$.ajax({
			url: this.feedServer + '/burrito/feeds/subscription/'+this.subscriptionId+'/keepAlive',
			crossDomain: true,
			dataType: "jsonp"
		});
		this.triggerNewKeepAlive();
	}
	
	this.triggerNewKeepAlive = function() {
		setTimeout(function() {
			object.keepAlive();
		}, 180000); // three minutes
	}

	this.getSubscriptionId = function() {
		return this.subscriptionId;
	}
}

//create global object accessible by page
var burritoFeeds = new BurritoFeeds();

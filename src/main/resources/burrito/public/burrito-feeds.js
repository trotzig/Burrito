/**
 * Channel implementation using polling
 * 
 * @param subscriptionId
 * @returns {BurritoPollingChannel}
 */
function BurritoPollingChannel(subscriptionId, feedServer) {
	
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
		var parent = this;
		$.ajax({
			url: this.feedServer + '/burrito/feeds/subscription/'+this.subscriptionId+'/poll', 
			dataType: "jsonp",
			crossDomain: true,
			success: function(json) {
				if (json.status == 'error') {
					throw("Error response from feed server: " + json.message);
				}
				parent.onMessages(json);
			}
		});
	}
	
	this.triggerNewPoll = function() {
		var parent = this;
		this.currentTimeout = setTimeout(function() {
			parent.doPoll();
			parent.triggerNewPoll();
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
	
	this.pendingHandlers = new Array();
	this.registeredHandlers = new Array();
	
	this.feedServer = ''; //defaults to same domain as website
	this.method = 'push';
	this.channelId = '';
	this.subscriptionId = -1;
	this.channelRequestSent = false;
	this.currentKeepAliveTimer = -1;

	/**
	 * Registers a new feed handler
	 */
	this.registerHandler = function(feedId, callback) {
		if (this.subscriptionId < 0) {
			this.pendingHandlers[feedId] = callback;

			if (!this.channelRequestSent) {	
				this.channelRequestSent = true;
				this.getSubscriptionAndOpenChannel();
			}
		}
		else {
			this.registeredHandlers[feedId] = callback;
			$.ajax({
				url: this.feedServer + '/burrito/feeds/subscription/' + this.subscriptionId + '/addFeed/' + encodeURIComponent(feedId),
				crossDomain: true,
				dataType: "jsonp"
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
		var parent = this;
		$.ajax({
			url: this.feedServer + '/burrito/feeds/subscription/new/' + encodeURIComponent(this.method), 
			dataType: "jsonp",
			crossDomain: true,
			success: function(json) {
				parent.onSubscriptionReceived(json);
			}
		});
	}
	
	this.onSubscriptionReceived = function(json) {
		if (json.status == 'error') {
			throw("Error response from feed server: " + json.message);
		}
		this.channelId = json.channelId;
		this.subscriptionId = json.subscriptionId;
		
		//iterate through all non-initialized handlers and make sure they are initialized
		for (var feedId in this.pendingHandlers) {
			this.registerHandler(feedId, this.pendingHandlers[feedId]);
			this.pendingHandlers[feedId] = null;
		}
		if (this.channelId) {
			this.openGoogleChannel();
		} else {
			this.openPollingChannel();
		}
	}
	
	this.renewSubscriptionAndOpenChannel = function() {
		clearTimeout(this.currentKeepAliveTimer);
		var parent = this;
		$.ajax({
			url: this.feedServer + '/burrito/feeds/subscription/'+this.subscriptionId+'/newChannel', 
			dataType: "jsonp",
			crossDomain: true,
			success: function(json) {
				parent.onSubscriptionReceived(json);
			}
		});
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
		var parent = this;
		var pollingChannel = new BurritoPollingChannel(this.subscriptionId, this.feedServer);
		var socket = pollingChannel.open();
		socket.onmessage = function(json) {
			parent.onMessageReceived(json);
		}
	}
	
	this.openGoogleChannel = function() {
		var parent = this;
		var channel = new goog.appengine.Channel(this.channelId);  
		var socket = channel.open();  
		socket.onopen = function() {  
		   //do something?  
			parent.triggerNewKeepAlive()
		}  
		 
		socket.onclose = function(evt) {
		 	//attempt to recreate channel
			parent.renewSubscriptionAndOpenChannel();
		}
		socket.onmessage = function(evt) {
		 	var json = eval("(" + evt.data + ")");
		 	parent.onMessageReceived(json);
		} 

		socket.onerror = function(error) {
			//do something!?
		} 
	}
	
	this.keepAlive = function() {
		$.ajax({
			url: '/burrito/feeds/subscription/'+this.subscriptionId+'/keepAlive',
			crossDomain: true,
			dataType: "jsonp"
		});
		this.triggerNewKeepAlive();
	}
	
	this.triggerNewKeepAlive = function() {
		var parent = this;
		currentKeepAliveTimer = setTimeout(function() {
				parent.keepAlive();
			}, 180000); // three minutes
	}
	
	this.getSubscriptionId = function() {
		return this.subscriptionId;
	}
	
}

var burritoFeeds = new BurritoFeeds();


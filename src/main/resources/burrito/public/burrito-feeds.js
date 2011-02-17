
/**
 * Burrito Feeds Object
 * 
 * @returns
 */
function BurritoFeeds() {
	
	this.pendingHandlers = new Array();
	this.registeredHandlers = new Array();
	
	this.feedServer = ''; //defaults to same domain as website
	this.method = 'poll';
	this.channelId = '';
	this.subscriptionId = -1;
	this.channelRequestSent = false;

	/**
	 * Registers a new feed handler
	 */
	this.registerHandler = function(feedId, callback) {
		if (this.channelId == '') {
			this.pendingHandlers[feedId] = callback;

			if (!this.channelRequestSent) {	
				this.channelRequestSent = true;
				this.getSubscriptionAndOpenChannel();
			}
		}
		else {
			this.registeredHandlers[feedId] = callback;
			$.ajax({
				url: this.feedServer + '/burrito/feeds/subscription/' + this.subscriptionId + '/addFeed/' + feedId,
				crossDomain: true,
				dataType: "jsonp"
			});
		}
	}
	
	/**
	 * Overrides the default channel server
	 */
	this.setFeedServer = function(chServer) {
		channelServer = chServer;
	}
	
	this.getSubscriptionAndOpenChannel = function() {
		var parent = this;
		$.ajax({
			url: this.feedServer + '/burrito/feeds/subscription/new', 
			dataType: "jsonp",
			crossDomain: true,
			success: function(json) {
				if (json.status == 'error') {
					alert(json.message);
					return;
				}
				parent.channelId = json.channelId;
				parent.subscriptionId = json.subscriptionId;
				for (var feedId in parent.pendingHandlers) {
					parent.registerHandler(feedId, parent.pendingHandlers[feedId]);
					parent.pendingHandlers[feedId] = null;
				}
				parent.openGoogleChannel();
			}
		});
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
			parent.getSubscriptionAndOpenChannel();
		}
		socket.onmessage = function(evt) {
		 	var json = eval("(" + evt.data + ")");
		 	var targetFeedId = json.feedId;
		 	for (var feedId in parent.registeredHandlers) {
		 		if (feedId == targetFeedId) {
		 			var callback = parent.registeredHandlers[feedId];
		 			callback(json.message);
		 		}
			}
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
		setTimeout("burritoFeeds.keepAlive()", 180000);
	}
	
	this.getSubscriptionId = function() {
		return this.subscriptionId;
	}
	
}

var burritoFeeds = new BurritoFeeds();


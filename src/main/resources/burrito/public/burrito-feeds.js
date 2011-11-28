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







/**
 * Channel implementation using polling
 * 
 * @param subscriptionId
 * @param feedServer
 * @returns {BurritoPollingChannel}
 */
function BurritoPollingChannel(subscriptionId, feedServer) {
	var object = this;

	this.pollingIntervalSeconds = 45;

	this.subscriptionId = subscriptionId;
	this.currentTimeout = -1;
	this.feedServer = feedServer;
	
	this.onmessage = function(json){}; //callback for messages
	
	this.open = function() {
		object.triggerNewPoll();
		return this;
	}
	
	this.onMessages = function(json) {
		if (!json.messages) {
			return;
		}
		//loop through all messages received and call the onmessage callback
		for (var i = 0; i < json.messages.length; i++) {
			var msgJson = json.messages[i];
			object.onmessage(msgJson);
		}
	}
	
	this.doPoll = function() {
		$.ajax({
			url: object.feedServer + '/burrito/feeds/subscription/' + object.subscriptionId + '/poll', 
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
		object.currentTimeout = setTimeout(function() {
			object.doPoll();
			object.triggerNewPoll();
		}, object.pollingIntervalSeconds * 1000);
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
	this.channelOpenCallback = function() {};

	this.onChannelOpen = function(func) {
		this.channelOpenCallback = func;
	};
	
	/**
	 * Registers a new feed handler
	 */
	this.registerHandler = function(feedId, callback) {
		if (object.subscriptionId < 0) {
			object.pendingHandlers[feedId] = callback;

			if (!object.subscriptionRequestSent) {	
				object.subscriptionRequestSent = true;
				object.getSubscriptionAndOpenChannel();
			}
		}
		else {
			object.registeredHandlers[feedId] = callback;
			object.addFeedToSubscription(feedId);
		}
	}

	this.addFeedToSubscription = function(feedId) {
		if (object.addFeedToSubscriptionLock) {
			setTimeout(function() {
				object.addFeedToSubscription(feedId);
			}, 200);
		}
		else {
			object.addFeedToSubscriptionLock = true;

			$.ajax({
				url: object.feedServer + '/burrito/feeds/subscription/' + object.subscriptionId + '/addFeed/' + encodeURIComponent(feedId),
				crossDomain: true,
				dataType: "jsonp",
				success: function(json) {
					object.addFeedToSubscriptionLock = false;
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
		object.feedServer = chServer;
	}
	
	/**
	 * Sets the method used for subscribing to feeds. Either 'poll' or 'push'. 
	 * The feed server will try to respect your setting, but it is not guaranteed. 
	 */
	this.setMethod = function(mthd) {
		//Just noticed that the channel api won't work in internet explorer 9. 
		//Therefore, we force poll if the talkkgadget.js code hasn't been loaded properly.
		if (typeof(goog) == 'undefined') {
			object.method = "poll";
		} else {
			object.method = mthd;
		}
	}
	
	this.getSubscriptionAndOpenChannel = function() {		
		var url = object.feedServer + '/burrito/feeds/subscription/new/' + encodeURIComponent(object.method);
		if (object.method == 'push') {
			var channelId = object.getCookie('burrito_unloadedChannelId');
			if (channelId) {
				object.deleteCookie('burrito_unloadedChannelId');
				url += '/' + encodeURIComponent(channelId);
			}
		}
		$.ajax({
			url: url, 
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
			url: object.feedServer + '/burrito/feeds/subscription/' + object.subscriptionId + '/newChannel', 
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
			url: object.feedServer + '/burrito/feeds/subscription/' + object.subscriptionId + '/dropChannel', 
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
		object.channelId = channelId;
		if (channelId) {
			if (!object.unloadHandlerAttached) {
				object.unloadHandlerAttached = true;
				$(window).unload(function() {
					if (object.channelId) {
						object.setCookie('burrito_unloadedChannelId', object.channelId, 5 * 60 * 60 * 1000);
					}
				});
			}
			object.openGoogleChannel();
		} else {
			object.openPollingChannel();
		}
	}

	this.onMessageReceived = function(json) {
		var targetFeedId = json.feedId;
	 	for (var feedId in object.registeredHandlers) {
	 		if (feedId == targetFeedId) {
	 			var callback = object.registeredHandlers[feedId];
	 			callback(json.message);
	 		}
		}
	}
	
	this.openPollingChannel = function() {
		var pollingChannel = new BurritoPollingChannel(object.subscriptionId, object.feedServer);
		var socket = pollingChannel.open();
		this.channelOpenCallback();
		socket.onmessage = function(json) {
			object.onMessageReceived(json);
		}
	}

	this.openGoogleChannel = function() {
		var lastChannelRetryTime = 0;
		var channel = new goog.appengine.Channel(object.channelId);
		var socket = channel.open();

		socket.onopen = function() {
			object.channelOpenCallback();
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
			url: object.feedServer + '/burrito/feeds/subscription/' + object.subscriptionId + '/keepAlive',
			crossDomain: true,
			dataType: "jsonp"
		});
		object.triggerNewKeepAlive();
	}
	
	this.triggerNewKeepAlive = function() {
		setTimeout(function() {
			object.keepAlive();
		}, 180000); // three minutes
	}

	this.getSubscriptionId = function() {
		return object.subscriptionId;
	}

	this.setCookie = function(name, value, expireInMillis) {
		var c = name + '=' + value;
		if (expireInMillis) {
			var date = new Date();
			date.setTime(date.getTime() + expireInMillis);
			c += '; expires=' + date.toGMTString();
		}
		document.cookie = c + '; path=/';
	}

	this.deleteCookie = function(name) {
		object.setCookie(name, '', -60 * 60 * 1000);
	}

	this.getCookie = function(name) {
		name += '=';
		var cookies = document.cookie.split(';');
		for (var i = 0; i < cookies.length; i++) {
			var c = $.trim(cookies[i]);
			if (c.indexOf(name) == 0) return c.substring(name.length);
		}
		return null;
	}
}

//create global object accessible by page
var burritoFeeds = new BurritoFeeds();
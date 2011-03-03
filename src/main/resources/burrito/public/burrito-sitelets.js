
function BurritoUtils() {
	var self = this;

	self.didRunOnceIdentifiers = new Array();

	self.jsFilesLoaded = new Array();
	self.jsFilesStartedLoading = new Array();
	self.jsFilesPendingCallbacks = new Array();

	this.runOnce = function(identifier, callback) {
		if (!self.didRunOnceIdentifiers[identifier]) {
			self.didRunOnceIdentifiers[identifier] = true;
			callback();
		}
	}

	this.runWithJavascriptFile = function(jsFileUrl, callback) {
		if (self.jsFilesLoaded[jsFileUrl]) {
			callback();
		}
		else {
			var pendingCallbacks = self.jsFilesPendingCallbacks[jsFileUrl];
			if (!pendingCallbacks) {
				pendingCallbacks = new Array();
				self.jsFilesPendingCallbacks[jsFileUrl] = pendingCallbacks;
			}
			pendingCallbacks.push(callback);

			if (!self.jsFilesStartedLoading[jsFileUrl]) {
				self.jsFilesStartedLoading[jsFileUrl] = true;

				$.getScript(jsFileUrl, function() {
					self.jsFilesLoaded[jsFileUrl] = true;

					for (var i = 0; i < pendingCallbacks.length; i++) {
						pendingCallbacks[i]();
					}
				});
			}
		}
	}

	this.getClassValue = function(element, prefix) {
		var classes = element.attr('class').split(' ');
		for (var i = 0; i < classes.length; i++) {
			var c = classes[i];
			if (c.length >= prefix.length && c.substring(0, prefix.length) == prefix) {
				return c.substring(prefix.length);
			}
		}
		return null;
	}
}

var burritoUtils = new BurritoUtils();

function BurritoSitelets() {
	var self = this;

	var adminControlsClass = 'sitelet-admin-controls';

	self.onNewSiteletContentCallbacks = new Array();

	this.registerLiveBox = function(siteIdentifier, boxId) {
		burritoFeeds.registerHandler('burrito:sitelet-box:' + siteIdentifier + '|' + boxId, function(message) {
			self.handleIncomingUpdate(boxId, eval('(' + message + ')'));
		});
	}

	this.handleIncomingUpdate = function(boxId, update) {
		var sitelets = update.sitelets;

		var box = $('#sitelet-box-' + boxId);

		box.find('.sitelet').each(function() {
			var id = burritoUtils.getClassValue($(this), 'sitelet-properties-id-');
			for (var i = 0; i < sitelets.length; i++) {
				if (id == sitelets[i].id) return;
			}
			$(this).removeClass('sitelet');
			$(this).slideUp(500, function() {
				$(this).remove();
			});
		});

		var previousWrapper = null;

		for (var i = 0; i < sitelets.length; i++) {
			var sitelet = sitelets[i];
			var idClassName = 'sitelet-properties-id-' + sitelet.id;
			var siteletWrapper = box.find('.sitelet.' + idClassName);

			if (siteletWrapper.length) {
				self.placeSiteletWrapper(box, previousWrapper, siteletWrapper, idClassName);
			}

			if (sitelet.html) {
				var hasNewContent;

				if (siteletWrapper.length) {
					var existingVersion = burritoUtils.getClassValue(siteletWrapper, 'sitelet-version-');
					hasNewContent = sitelet.version > existingVersion;
					if (hasNewContent) {
						siteletWrapper.html(sitelet.html);
						siteletWrapper.removeClass('sitelet-version-' + existingVersion);
						siteletWrapper.addClass('sitelet-version-' + sitelet.version);
					}
				}
				else {
					hasNewContent = true;
					siteletWrapper = '<div style="display: none" class="sitelet ' + idClassName + ' sitelet-version-' + sitelet.version + '">' + sitelet.html + '</div>';
					self.placeSiteletWrapper(box, previousWrapper, siteletWrapper, idClassName);
					siteletWrapper = box.find('.sitelet.' + idClassName);
					siteletWrapper.slideDown(500);
				}

				if (hasNewContent) {
					for (var j = 0; j < self.onNewSiteletContentCallbacks.length; j++) {
						var callback = self.onNewSiteletContentCallbacks[j];
						callback(siteletWrapper);
					}
				}
			}
			else if (sitelet.version > burritoUtils.getClassValue(siteletWrapper, 'sitelet-version-')) {
				if (!self.boxPollTimeout) {
					alert("polling for box contents soon");
					self.boxPollTimeout = setTimeout(function() {
						alert("polling for box contents now");
						self.boxPollTimeout = false;
						$.ajax({
							url: '/burrito/sitelets/box/' + boxId + '/poll',
							dataType: "jsonp",
							success: function(json) {
								if (json.status == 'error') {
									throw("Error response from feed server: " + json.message);
								}
								self.handleIncomingUpdate(boxId, json);
							}
						});
					}, 100 + Math.floor(60 * 1000 * Math.random()));
				}
			}

			previousWrapper = siteletWrapper;
		}
	}

	this.placeSiteletWrapper = function(box, previousWrapper, siteletWrapper, idClassName) {
		if (previousWrapper) {
			if (!previousWrapper.nextAll('.sitelet:first').hasClass(idClassName)) {
				previousWrapper.after(siteletWrapper);
			}
		}
		else {
			if (!box.children('.sitelet:first').hasClass(idClassName)) {
				box.prepend(siteletWrapper);
			}
		}
	}

	this.refreshSitelet = function(siteletPropertiesId) {
		$.ajax({url: '/burrito/sitelets/refresh/' + siteletPropertiesId + '?force=true'});
	}

	this.onNewSiteletContent = function(callback) {
		self.onNewSiteletContentCallbacks.push(callback);
	}

	this.enableAdminControls = function() {
		$('.' + adminControlsClass).live('click', function() {
			var siteletPropertiesId = burritoUtils.getClassValue($(this), 'sitelet-properties-id-');

			$(this).fadeOut(function() {
				$(this).fadeIn();
			});

			self.refreshSitelet(siteletPropertiesId)
		});

		$('.sitelet').live('mouseover mouseout', function(event) {
			if (event.type == 'mouseover') {
				if (!self.adminControlsTimeout) {
					var siteletPropertiesId = burritoUtils.getClassValue($(this), 'sitelet-properties-id-');

					if (!$('.' + adminControlsClass + '.sitelet-properties-id-' + siteletPropertiesId).length) {
						var offset = $(this).offset();
						self.adminControlsTimeout = setTimeout(function() {
							$('.' + adminControlsClass).remove();
							$('body').append('<div class="' + adminControlsClass + ' sitelet-properties-id-' + siteletPropertiesId + '" style="top: ' + offset.top + 'px; left: ' + offset.left + 'px; position: absolute">Refresh</div>');
							self.adminControlsTimeout = false;
						}, 1000);
					}
				}
			}
			else {
				if (self.adminControlsTimeout) {
					clearTimeout(self.adminControlsTimeout);

					self.adminControlsTimeout = false;
				}
			}
		});

		$('.' + adminControlsClass).live('mouseout', function() {
			$(this).remove();
		});
	}

	this.runOnceForSiteletVersion = function(siteletPropertiesId, renderedVersion, callback) {
		burritoUtils.runOnce('burrito-sitelet-' + siteletPropertiesId + '-version-' + renderedVersion, callback);
	}
}

var burritoSitelets = new BurritoSitelets();

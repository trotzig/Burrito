
function BurritoSitelets() {
	var object = this;

	var adminControlsClass = 'sitelet-admin-controls';

	object.onNewSiteletContentCallbacks = new Array();

	this.registerLiveBox = function(siteIdentifier, boxId) {
		burritoFeeds.registerHandler('burrito:sitelet-box:' + siteIdentifier + '|' + boxId, function(message) {
			message = eval('(' + message + ')');
			var sitelets = message.sitelets;

			var box = $('#sitelet-box-' + boxId);

			box.find('.sitelet').each(function() {
				var id = object.getClassValue($(this), 'sitelet-properties-id-');
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
					object.placeSiteletWrapper(box, previousWrapper, siteletWrapper, idClassName);
				}

				if (sitelet.html) {
					if (siteletWrapper.length) {
						siteletWrapper.html(sitelet.html);
					}
					else {
						siteletWrapper = '<div style="display: none" class="sitelet ' + idClassName + '">' + sitelet.html + '</div>';
						object.placeSiteletWrapper(box, previousWrapper, siteletWrapper, idClassName);
						siteletWrapper = box.find('.sitelet.' + idClassName);
						siteletWrapper.slideDown(500);
					}

					for (var j = 0; j < object.onNewSiteletContentCallbacks.length; j++) {
						var callback = object.onNewSiteletContentCallbacks[j];
						callback(siteletWrapper);
					}
				}

				previousWrapper = siteletWrapper;
			}
		});
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
		object.onNewSiteletContentCallbacks.push(callback);
	}

	this.enableAdminControls = function() {
		$('.' + adminControlsClass).live('click', function() {
			var siteletPropertiesId = object.getClassValue($(this), 'sitelet-properties-id-');

			$(this).fadeOut(function() {
				$(this).fadeIn();
			});

			object.refreshSitelet(siteletPropertiesId)
		});

		$('.sitelet').live('mouseover mouseout', function(event) {
			if (event.type == 'mouseover') {
				if (!object.adminControlsTimeout) {
					var siteletPropertiesId = object.getClassValue($(this), 'sitelet-properties-id-');

					if (!$('.' + adminControlsClass + '.sitelet-properties-id-' + siteletPropertiesId).length) {
						var offset = $(this).offset();
						object.adminControlsTimeout = setTimeout(function() {
							$('.' + adminControlsClass).remove();
							$('body').append('<div class="' + adminControlsClass + ' sitelet-properties-id-' + siteletPropertiesId + '" style="top: ' + offset.top + 'px; left: ' + offset.left + 'px; position: absolute">KOLLA HÄR: ' + siteletPropertiesId + '</div>');
							object.adminControlsTimeout = false;
						}, 1000);
					}
				}
			}
			else {
				if (object.adminControlsTimeout) {
					clearTimeout(object.adminControlsTimeout);

					object.adminControlsTimeout = false;
				}
			}
		});

		$('.' + adminControlsClass).live('mouseout', function() {
			$(this).remove();
		});
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

var burritoSitelets = new BurritoSitelets();

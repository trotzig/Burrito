
function BurritoSitelets() {

	var adminControlsClass = 'sitelet-admin-controls';

	this.onNewSiteletContentCallbacks = [];

	this.registerLiveBox = function(siteIdentifier, boxId) {
		burritoFeeds.registerHandler('burrito:sitelet-box:' + siteIdentifier + '|' + boxId, function(message) {
			message = eval('(' + message + ')');
			var sitelets = message.sitelets;

			var box = $('#sitelet-box-' + boxId);

			box.find('.sitelet').each(function() {
				for (var i = 0; i < sitelets.length; i++) {
					if ($(this).hasClass('sitelet-properties-id-' + sitelets[i].id)) return;
				}
				$(this).css('class',null);
				$(this).slideUp(500, function() {
					$(this).remove();
				});
			});

			for (var i = 0; i < sitelets.length; i++) {
				var sitelet = sitelets[i];
				var className = 'sitelet-properties-id-' + sitelet.id;
				var siteletContainer = box.find('.sitelet.' + className);
				if (sitelet.html) {
					if (siteletContainer.length) {
						siteletContainer.html(sitelet.html);
					}
					else {
						box.append('<div style="display: none" class="sitelet ' + className + '">' + sitelet.html + '</div>');
						siteletContainer = box.find('.sitelet.' + className);
						siteletContainer.slideDown(500);
					}

					for (var i = 0; i < this.onNewSiteletContentCallbacks.length; i++) {
						var callback = this.onNewSiteletContentCallbacks[i];
						callback(siteletContainer);
					}
				}
				else {
					box.append(siteletContainer);
				}
			}
		});
	}

	this.refreshSitelet = function(siteletPropertiesId) {
		$.ajax({url: '/burrito/sitelets/refresh/' + siteletPropertiesId + '?force=true'});
	}

	this.onNewSiteletContent = function(callback) {
		this.onNewSiteletContentCallbacks.push(callback);
	}

	this.enableAdminControls = function() {
		var parent = this;

		$('.' + adminControlsClass).live('click', function() {
			var siteletPropertiesId = getClassValue($(this), 'sitelet-properties-id-');

			$(this).fadeOut(function() {
				$(this).fadeIn();
			});

			parent.refreshSitelet(siteletPropertiesId)
		});

		$('.sitelet').live('mouseover mouseout', function(event) {
			if (event.type == 'mouseover') {
				if (!this.adminControlsTimeout) {
					var siteletPropertiesId = getClassValue($(this), 'sitelet-properties-id-');

					if (!$('.' + adminControlsClass + '.sitelet-properties-id-' + siteletPropertiesId).length) {
						var offset = $(this).offset();
						this.adminControlsTimeout = setTimeout(function() {
							$('.' + adminControlsClass).remove();
							$('body').append('<div class="' + adminControlsClass + ' sitelet-properties-id-' + siteletPropertiesId + '" style="top: ' + offset.top + 'px; left: ' + offset.left + 'px; position: absolute">KOLLA HÄR: ' + siteletPropertiesId + '</div>');
							this.adminControlsTimeout = false;
						}, 1000);
					}
				}
			}
			else {
				if (this.adminControlsTimeout) {
					clearTimeout(this.adminControlsTimeout);

					this.adminControlsTimeout = false;
				}
			}
		});

		$('.' + adminControlsClass).live('mouseout', function() {
			$(this).remove();
		});
	}
}

function getClassValue(element, prefix) {
	var classes = element.attr('class').split(' ');
	for (var i = 0; i < classes.length; i++) {
		var c = classes[i];
		if (c.length >= prefix.length && c.substring(0, prefix.length) == prefix) {
			return c.substring(prefix.length);
		}
	}
	return null;
}

var burritoSitelets = new BurritoSitelets();

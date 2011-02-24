
function BurritoSitelets() {

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
				var element = box.find('.' + className);
				if (element.length) {
					if (sitelet.html) element.html(sitelet.html);
					box.append(element);
				}
				else if (sitelet.html) {
					box.append('<div style="display: none" class="sitelet ' + className + '">' + sitelet.html + '</div>');
					box.find('.' + className).slideDown(500);
				}
			}
		});
	}

	this.refreshSitelet = function(siteletPropertiesId) {
		$.ajax({url: '/burrito/sitelets/refresh/' + siteletPropertiesId + '?force=true'});
	}
}

var burritoSitelets = new BurritoSitelets();

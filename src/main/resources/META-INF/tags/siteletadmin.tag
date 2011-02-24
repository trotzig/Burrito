<%@tag import="burrito.util.StringUtils"%>
<%@ tag isELIgnored="false" pageEncoding="UTF-8" %>
<%@ taglib prefix="burrito" uri="/burrito-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="siteletProperties" required="true" type="burrito.services.SiteletProperties"%>

<div class="sitelet-admin-controls">
	<a href="#" onclick="$(this).fadeOut(function(){$(this).fadeIn()});burritoSitelets.refreshSitelet(${siteletProperties.id});return false;" title="<burrito:message key="sitelet_admin_refresh_desc"/>">
		<burrito:message key="sitelet_admin_refresh"/>
	</a>
	<div class="clear-floats"></div>
</div>

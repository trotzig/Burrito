<%@tag import="burrito.util.FriendlyDateFormatter"%>
<%@ tag isELIgnored="false" pageEncoding="UTF-8" %>
<%@ taglib prefix="burrito" tagdir="/WEB-INF/tags/burrito" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="key" required="true" %>
<%@ attribute name="expirationSeconds" required="true" type="java.lang.Integer" %>
<%
	Integer expiration = (Integer)jspContext.getAttribute("expirationSeconds");
	String expString = FriendlyDateFormatter.formatSeconds(expiration);
%>

<div class="cache-admin-controls">
	<ul>
		<li>
			<burrito:message key="cache_admin_header" extra="<%= expString %>"/>
		</li>
		<li id="clearcache-${key}-reloadpage" style="display: none">
			&nbsp;|&nbsp;
			<a href="javascript:window.location.reload()">Ladda om sidan</a>
		</li>
		<li>&nbsp;|&nbsp;</li>
		<li>
			<a href="/admin/clearcache?key=${key}" class="cache-admin-controls-clearcache" id="clearcache-${key}" title="${key}">
				<c:set var="desc"><burrito:message key="cache_admin_clear_cache_desc"/></c:set>
				<span title="${desc}"><burrito:message key="cache_admin_clear_cache"/></span>
			</a>
		</li>
		<li id="clearcache-${key}-progress" style="text-decoration:blink;display:none;float:left">
			...
		</li>
		<li id="clearcache-${key}-done" style="display:none;float:left">
			Cachen&nbsp;rensad
		</li>
	</ul>
	<div class="clear-floats"></div>
</div>
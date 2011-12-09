<%@tag import="java.util.Date"%>
<%@tag import="burrito.render.RefreshSiteletRenderer"%>
<%@tag import="burrito.sitelet.Sitelet"%>
<%@ tag isELIgnored="false" body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="burrito" uri="/burrito-tags" %>
<%@ tag import="java.util.List"%>
<%@ tag import="burrito.util.SiteletHelper"%>
<%@ tag import="burrito.services.SiteletProperties"%>
<%@ tag import="burrito.util.StringUtils"%>
<%@ tag import="burrito.Configurator"%>

<%@ attribute name="id" required="true" %>
<%@ attribute name="live" required="false" %>
<%@ attribute name="addOnTop" required="false" type="java.lang.Boolean" %>
<c:if test="${taco_userIsAdmin}">
	<burrito:siteletboxadmin id="${id}" addOnTop="${empty addOnTop ? false : addOnTop}"/>
</c:if>

<div id="sitelet-box-${id}" class="sitelet-container">
	<%
		String id = (String)jspContext.getAttribute("id");
		List<SiteletProperties> sitelets = SiteletHelper.getSiteletProperties(id); 
		for (SiteletProperties siteletProperties : sitelets) {
			String html;
			if (siteletProperties.isRetired()) {
				//un-retire the sitelet:
				RefreshSiteletRenderer.refreshSitelet(siteletProperties, false, request, response);
			} 
			html = siteletProperties.renderedHtml;
			SiteletHelper.touchSiteletLastDisplayTime(siteletProperties.getId());
			%>
				<div class="sitelet sitelet-properties-id-${siteletProperties.id} sitelet-version-${empty siteletProperties.renderedVersion ? 0 : siteletProperties.renderedVersion}">
				<%
					out.write(html);
				%>
				</div>
			<% 
		}
	%>
</div>

<c:if test="${live}">
	<script type="text/javascript">
		burritoSitelets.registerLiveBox("<%= StringUtils.escapeJavascript(Configurator.getSiteIdentifier()) %>", "<%= StringUtils.escapeJavascript(id) %>");
	</script>
</c:if>

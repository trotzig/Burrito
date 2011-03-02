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

<c:if test="${taco_userIsAdmin}">
	<burrito:siteletboxadmin id="${id}"/>
</c:if>

<div id="sitelet-box-${id}" class="sitelet-container">
	<%
		String id = (String)jspContext.getAttribute("id");
		List<SiteletProperties> sitelets = SiteletHelper.getSiteletProperties(id); 
		for (SiteletProperties siteletProperties : sitelets) {
			jspContext.setAttribute("siteletProperties", siteletProperties);
			%>
				<div class="sitelet sitelet-properties-id-${siteletProperties.id} sitelet-version-${empty siteletProperties.renderedVersion ? 0 : siteletProperties.renderedVersion}">
					${siteletProperties.renderedHtml}
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

<%@ tag isELIgnored="false" body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib prefix="burrito" uri="/burrito-tags" %>
<%@ attribute name="id" required="true" %>

<%@tag import="java.util.List"%>
<%@tag import="burrito.util.SiteletHelper"%>
<%@tag import="burrito.services.SiteletProperties"%>
<%@tag import="burrito.sitelet.Sitelet"%>
<%@tag import="burrito.util.StringUtils"%>
<%@tag import="burrito.Configurator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
		<c:if test="${taco_userIsAdmin}">
			<burrito:siteletadmin siteletProperties="${siteletProperties}"/>
		</c:if>
		<div class="sitelet sitelet-properties-id-${siteletProperties.id}">
			${siteletProperties.renderedHtml}
		</div>
<%
	}
%>
</div>

<script type="text/javascript">
	burritoSitelets.registerLiveBox("<%= StringUtils.escapeJavascript(Configurator.getSiteIdentifier()) %>", "<%= StringUtils.escapeJavascript(id) %>");
</script>

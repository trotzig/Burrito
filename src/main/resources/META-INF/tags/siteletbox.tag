<%@ tag isELIgnored="false" body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib prefix="burrito" uri="/burrito-tags" %>
<%@ attribute name="id" required="true" %>

<%@tag import="java.util.List"%>
<%@tag import="burrito.util.SiteletHelper"%>
<%@tag import="burrito.services.SiteletProperties"%>
<%@tag import="burrito.sitelet.Sitelet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${taco_userIsAdmin}">
	<burrito:siteletboxadmin id="${id}"/>
</c:if>
<div id="sitelet-box-${id}" class="sitelet-container">

<%
	List<SiteletProperties> sitelets = SiteletHelper.getSiteletProperties((String)jspContext.getAttribute("id")); 
	for (SiteletProperties siteletProperties : sitelets) {
		jspContext.setAttribute("siteletProperties", siteletProperties);
%>
		${siteletProperties.renderedHtml}
<%
	}
%>
</div>


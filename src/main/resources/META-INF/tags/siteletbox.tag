<%@ tag isELIgnored="false" body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib prefix="burrito" tagdir="/WEB-INF/tags/burrito" %>
<%@ attribute name="id" required="true" %>

<%@tag import="java.util.List"%>
<%@tag import="burrito.util.SiteletHelper"%>
<%@tag import="burrito.services.SiteletProperties"%>
<%@tag import="burrito.sitelet.Sitelet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${mvcaur_userIsAdmin}">
	<burrito:siteletboxadmin id="${id}"/>
</c:if>
<div id="${id}" class="sitelet-container">

<%
	List<SiteletProperties> sitelets = SiteletHelper.getSiteletProperties((String)jspContext.getAttribute("id")); 
	int i = 1;
	for (SiteletProperties siteletProperties : sitelets) {
		jspContext.setAttribute("siteletProperties", siteletProperties);
%>
	<c:choose>
		<c:when test="${siteletProperties.cacheEnabled}">
			<burrito:cache key="${siteletProperties.cacheKey}" expirationSeconds="${siteletProperties.cacheExpirationInSeconds}">
				<burrito:sitelet sitelet="${siteletProperties.associatedSitelet}"/>
			</burrito:cache>
		</c:when>
		<c:otherwise>
			<burrito:sitelet sitelet="${siteletProperties.associatedSitelet}"/>
		</c:otherwise>
	</c:choose>
<%
		i++;
	}
%>
</div>


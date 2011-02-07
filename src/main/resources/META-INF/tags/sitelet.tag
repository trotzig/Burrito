<%@ tag pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib prefix="burrito" tagdir="/WEB-INF/tags/burrito" %>
<%@ attribute name="sitelet" type="burrito.sitelet.Sitelet" required="true" %>
<%
	burrito.sitelet.Sitelet sitelet = (burrito.sitelet.Sitelet)jspContext.getAttribute("sitelet");
	//exponera globalt för render.jsp att plocka upp
	jspContext.setAttribute("sitelet", sitelet, PageContext.REQUEST_SCOPE);
%>
<div class="sitelet-wrapper sitelet-wrapper-last-false">
	<div class="sitelet">
		<jsp:include page="/sitelets/${sitelet.class.simpleName}/render.jsp"/>
	</div>
</div>


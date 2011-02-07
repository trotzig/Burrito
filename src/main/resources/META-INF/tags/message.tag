<%@ tag isELIgnored="false" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="key" required="true" %>
<%@ attribute name="extra" required="false" %>
<fmt:setBundle basename="burrito.SiteletMessages"/>
<fmt:message var="msg" key="${key}"/>

<c:choose>
	<c:when test="${!empty extra}">
		<c:out value="${fn:replace(msg, '%s', extra)}"/>
	</c:when>
	<c:otherwise>
		<c:out value="${msg}"/>
	</c:otherwise>
</c:choose>

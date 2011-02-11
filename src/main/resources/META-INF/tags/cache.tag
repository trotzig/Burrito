<%@ tag isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="burrito" uri="/burrito-tags" %>
<%@ tag import="java.io.StringWriter" %>
<%@ tag import="burrito.util.Cache" %>

<%@ attribute name="key" required="true" %>
<%@ attribute name="expirationSeconds" required="true" type="java.lang.Integer" %>

<c:if test="${taco_userIsAdmin}">
	<burrito:cacheadmin key="${key}" expirationSeconds="${expirationSeconds}"/>
</c:if>

<div id="${key}">
	<%
		String key = (String) jspContext.getAttribute("key");
		Boolean doRecache = (Boolean) jspContext.getAttribute("doRecache", PageContext.REQUEST_SCOPE);

		String cachedOutput;
		if (doRecache != null && doRecache) cachedOutput = null;
		else cachedOutput = (String) Cache.get(key);

		if (cachedOutput == null) {
		    Integer expirationSeconds = (Integer) jspContext.getAttribute("expirationSeconds");

		    StringWriter buffer = new StringWriter();
		    getJspBody().invoke(buffer); // this bad boy messes up attributes in the jsp context
		    cachedOutput = buffer.toString();

		    Cache.put(key, cachedOutput, expirationSeconds);
		}

		jspContext.getOut().print(cachedOutput);
	%>
</div>

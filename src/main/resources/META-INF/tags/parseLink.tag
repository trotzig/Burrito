<%@ tag isELIgnored="false" body-content="empty" pageEncoding="UTF-8"%>
<%@ attribute name="link" required="true" %>
<%@tag import="burrito.links.JsonLinkParser"%>
<%@tag import="burrito.links.Link"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
/**this tag takes in a Json link (String) and uses JsonLinkParser to parse it.
Output from tag will be <a href="url">text</a>
*/

JsonLinkParser parser = new JsonLinkParser();
	
	Link parsedLink = parser.parseFailSafe((String)jspContext.getAttribute("link")); 
		
	jspContext.setAttribute("text", parsedLink.getText());
	jspContext.setAttribute("url", parsedLink.getUrl());
%>	
	<a href="<c:out value="${url}"/>"><c:out value="${text}"/></a>


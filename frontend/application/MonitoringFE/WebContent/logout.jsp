<%@page import="javax.servlet.http.HttpSession"%>
<%
HttpSession _session = request.getSession(false);
if (_session != null) {
	_session.invalidate();
}
request.logout();
response.sendRedirect(request.getRequestURI().replaceFirst("logout\\.jsp.*", ""));
%>
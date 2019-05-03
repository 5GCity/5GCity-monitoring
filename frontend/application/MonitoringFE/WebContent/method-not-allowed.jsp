<%
String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
if(requestUri.contains("/j_security_check")) {
	response.sendRedirect(requestUri.replaceFirst("/j_security_check.*", "/"));
}
%>

<!DOCTYPE html>
<html>
<head>
<title>Monitoring WebGui- Error</title> 
<meta name="description" content="Monitoring WebGui">
</head>
<body>
405: Method not allowed
</body>
</html>

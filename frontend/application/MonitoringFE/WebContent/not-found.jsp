<%
String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
if(requestUri.contains("/j_security_check")) {
	response.sendRedirect(requestUri.replaceFirst("/j_security_check.*", "/"));
}
%>

<!DOCTYPE html>
<html>
<head>
<title>Monitoring WebGui - Error</title>
<!--title>NetmatchS WebGui - Error</title-->
<meta name="description" content="Monitoring WebGui">
</head>
<body>
404: Not found
</body>
</html>

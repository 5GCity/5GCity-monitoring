<!DOCTYPE html>
<html>

<%@ page import="com.italtel.monitoring.fe.utils.OSUtils"%>
<%
    String portGrafana = OSUtils.getGrafanaPort();
%>

<head>
<title>Monitoring</title>
<meta name="description" content="Monitoring">
<jsp:include page="include/header.jsp" />
<link href="css/custom-scrollbar/jquery.mCustomScrollbar.css"
	rel="stylesheet" />
<link href="css/Treant/perfect-scrollbar.css" rel="stylesheet" />
<link href="css/Treant/Treant.css" rel="stylesheet" />
<style>
.content-wrapper {
	background-color: #222d32;
}
</style>
</head>
<body class="skin-blue sidebar-mini">
	<jsp:include page="include/top-nav.jsp" />
	<div class="wrapper" id="main-div">
		<aside class="main-sidebar">
			<section class="sidebar">
				<jsp:include page="include/user-box.jsp" />
				<jsp:include page="include/side-menu.jsp" />
			</section>
		</aside>
		<div class="content-wrapper">
			<section class="content" style="padding:0px;">
				<div class="display-message"></div>
				<iframe id="myIframe"
					style="width: 100%; height: 800px;border:0px;"></iframe>
			</section>
		</div>
		<jsp:include page="include/footer.jsp" />
		<jsp:include page="include/help-menu.jsp" />
	</div>
	<jsp:include page="include/scripts.jsp" />
	<script src="jslib/jquery-1.11.3.min.js"></script>
	<script >
       $(document).ready(function() {
    	   
    		    var strUrl = getDashBoardURL('dashboardUrl');
    		    var strName = "";       
    			if (strUrl == null) {
    				strName = 'http://' + window.location.hostname + ':' + <%=portGrafana%> +'/dashboard/db/summarynode?refresh=1h&orgId=1';
    			}
    			else {
    				strName = 'http://' + window.location.hostname + ':' + <%=portGrafana%> + strUrl;
    			}
    		    //console.log("strName : ",strName);
				$('#main-div').find('#myIframe').attr('src',strName);
       });
    </script>
</body>
</html>


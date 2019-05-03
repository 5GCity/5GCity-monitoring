<% response.addHeader("X-Frame-Options", "DENY"); %>
<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">
<head>
<meta charset="utf-8">
<meta
	content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no'
	name='viewport'>
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>Monitoring WebGui</title>

<link rel="icon" href="img/italtel.ico">
<link href="jslib/bootstrap-3.3.5/css/bootstrap.min.css"
	rel="stylesheet" media="screen">
<link href="css/AdminLTE.css" rel="stylesheet" type="text/css" />
<link href="css/skins/_all-skins.min.css" rel="stylesheet"
	type="text/css" />
<link href="css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="css/ionicons.min.css" rel="stylesheet" type="text/css" />
<link href="css/daterangepicker/daterangepicker-bs3.css"
	rel="stylesheet" type="text/css" />
<link href="css/iCheck/all.css" rel="stylesheet" type="text/css" />
<link href="css/colorpicker/bootstrap-colorpicker.min.css"
	rel="stylesheet" />
<link href="css/timepicker/bootstrap-timepicker.min.css"
	rel="stylesheet" />
<link href="css/twain.css" rel="stylesheet" type="text/css" />
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="jslib/html5shiv.js"></script>
      <script src="jslib/respond.min.js"></script>
    <![endif]-->

<link href="css/login.css" rel="stylesheet" media="screen"
	type="text/css">
	
<!-- START Legacy Browser Anti-ClickJacking Script -->
<style id="antiClickjack">body{display:none !important;}</style>    
<script type="text/javascript">
   if (self === top) {
       var antiClickjack = document.getElementById("antiClickjack");
       antiClickjack.parentNode.removeChild(antiClickjack);
   } else {
       top.location = self.location;
   }
</script> 
<!-- END Legacy Browser Anti-ClickJacking Script -->	

</head>

<!--[if IE]>
<body class="skin-blue">
<![endif]-->
<!--[if !IE]><!-->
<body class="skin-blue body-bg">
	<!--<![endif]-->

	<header class="main-header">
		<a class="logo" href="index.jsp">
			<span class="logo-lg">Monitoring WebGui</span>
		</a>
		<nav id="nav" class="navbar navbar-static-top" role="navigation">
		</nav>
	</header>
<!--
	<header class="main-header">
		<a class="navbar-brand logo" href="#"> <img class="logo-img"
			src="img/italtel-header2.png" />
		</a>
		<nav id="nav" class="navbar navbar-static-top" role="navigation">
		</nav>
	</header>
-->	
	<div class="container">
		<div class="row">
			<div class="col-lg-4 col-sm-4 col-lg-offset-3 col-sm-offset-3">
				<h3>SkyLine</h3> <br>
				<br class="hidden-xs"> <img class="img-responsive hidden-xs"
					src="img/images5Gmano.jpg" />

			</div>

			<div class="col-lg-4 col-sm-4 col-lg-offset-1 col-sm-offset-1">

				<c:set value="form-signin caption" var="cssClass"></c:set>
				<c:if test='${not empty param["Retry"]}'>
					<c:set
						value="form-signin form-group has-error has-feedback caption"
						var="cssClass"></c:set>
				</c:if>
				<form class="${cssClass}" action="j_security_check" method="post">
					<h3 class="form-signin-heading">Authentication</h3>  <br>
					<label for="j_username" class="sr-only">Username</label> <input
						type="text" name="j_username" value="Italtel" class="form-control"
						placeholder="Username" required autofocus value=""> <label
						for="j_password" class="sr-only">Password</label> <input
						type="password" name="j_password" value="Italtel123" class="form-control"
						placeholder="Password" required value="">
					<c:if test='${not empty param["Retry"]}'>
						<div class="form-error">
							<h4>Username or password you entered is incorrect.</h4>
						</div>
					</c:if>
					<button class="btn btn-lg btn-primary btn-block" type="submit"
						value="Login">Sign in</button>
				</form>
			</div>
		</div>
	</div>

</body>
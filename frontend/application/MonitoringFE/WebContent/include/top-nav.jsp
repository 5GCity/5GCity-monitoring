<%@ page import="com.italtel.monitoring.fe.security.CustomPrincipal"%>
<%
	String userRole = "SYS_ADMIN";
%>

<header class="main-header">

	<!-- Header Navbar: style can be found in header.less -->
	<nav class="navbar navbar-static-top navbar-fixed-top"
		style="margin-left: 0px;" role="navigation">
		<a class="logo" href="index.jsp">
			<span class="logo-lg">Monitoring WebGui</span>
		</a>
		<!-- Sidebar toggle button-->
		<a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas"
			role="button"> <span class="sr-only">Toggle navigation</span> <span
			class="glyphicon glyphicon-menu-hamburger" aria-hidden="true"></span>
		</a>
		<div class="navbar-custom-menu">
			<ul class="nav navbar-nav">
			<!--  
				<li class="dropdown user user-menu"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">
						<span class="glyphicon glyphicon-user"></span> <span
						id="logged_user" data-user-role="<%=userRole%>">Italtel</span>
				</a>
					<ul class="dropdown-menu">
						<li class="user-header" id="user-icon"><img
							src="img/user-icon.png" class="img-circle" alt="User Image">
							<p>
								Italtel
								<small><%=userRole%></small>
							</p></li>
						<li class="user-body" id="form-change-password">
							<div class="form-group data-input-l1">
								<label>Old Password</label><input
									class="form-control data-input input-sm" type="password"
									name="oldPassword">
							</div>
							<div class="form-group data-input-l1">
								<label>New Password</label><input
									class="form-control data-input input-sm" type="password"
									name="newPassword" id="newPassword">
							</div>
							<div class="form-group">
								<label>Retype New Password</label><input
									class="form-control data-input-skip input-sm" type="password"
									name="retypeNewPassword" id="retypeNewPassword">
							</div>
							<button class="btn btn-primary btn-flat btn-save">Change</button>
							<button class="btn btn-default btn-flat btn-cancel">Cancel</button>
						</li>
						<li class="user-body">
							<div class="col-xs-5 text-center user-deny admin-deny">
								<a href="user-management.jsp">Manage Users</a>
							</div>
							<div class="col-xs-7 text-center">
								<a href="#" id="change-password">Change Password</a>
							</div>
						</li>
						<li class="user-footer">
							<div class="pull-right">
								<a href="logout.jsp" class="btn btn-primary btn-flat">Sign
									out</a>
							</div>
						</li>
					</ul></li>
					-->
				<li><a href="#" data-toggle="control-sidebar"><i
						class="fa fa-question-circle"></i></a></li>
			</ul>
		</div>
	</nav>
</header>


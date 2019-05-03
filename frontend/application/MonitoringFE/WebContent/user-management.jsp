<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html>
<head>
<title>Monitoring</title>
<meta name="description" content="Monitoring">
<jsp:include page="include/header.jsp" />
<style>
</style>
</head>
<body
	class="skin-blue sidebar-mini <c:out value="${sessionScope.collapseClass}"/>">
	<jsp:include page="include/top-nav.jsp" />
	<div class="wrapper">
		<aside class="main-sidebar">
			<section class="sidebar">
				<jsp:include page="include/user-box.jsp" />
				<jsp:include page="include/side-menu.jsp" />
			</section>
		</aside>
		<div class="content-wrapper">
			<section class="content-header">
				<div class="row">
					<div class="col-sm-8">
						<h1 id="main-title">&nbsp;</h1>
					</div>
					<div class="col-sm-4" id="nav-toolbar"></div>
				</div>
			</section>
			<section class="content">
				<div class="display-message"></div>
				<div id="main-container">
					<div class="row">
						<div class="col-md-8">
							<div class="box box-primary" id="user-management">
								<div class="box-header">
									<h3 class="box-title">
										<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
										User Management
									</h3>
									<div class="box-tools pull-right"></div>
								</div>
								<div class="box-body"></div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="box box-warning" id="user-add">
								<div class="box-header">
									<h3 class="box-title">
										<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
										Add User
									</h3>
									<div class="box-tools pull-right"></div>
								</div>
								<div class="box-body">
									<div class="form-group data-input-l1">
										<label>Role</label> <select
											class="form-control input-sm data-input" name="role"><option
												value="SYSTEM_ADMIN">System Administrator</option>
											<option value="ADMIN">Administrator</option>											
											<option value="USER" selected="selected">User</option></select>
									</div>
									<div class="form-group data-input-l1">
										<label>Username</label><input
											class="form-control data-input input-sm" type="text"
											name="username">
									</div>
									<div class="form-group data-input-l1">
										<label>Password</label><input
											class="form-control data-input input-sm" type="password"
											name="passwd">
									</div>
									<div class="form-group data-input-l1">
										<label>Org</label><input
											class="form-control data-input input-sm" type="text"
											name="org">
									</div>
									<button class="btn btn-primary btn-flat btn-save">Save</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
		<jsp:include page="include/footer.jsp" />
		<jsp:include page="include/help-menu.jsp" />
	</div>
	<jsp:include page="include/scripts.jsp" />
	<script type="text/javascript">
		$(document).ready(function() {
			setPageTitle("User Management");
			console.log("user management");
			var objectName = getURLParameter('name');
			var operation = getURLParameter('operation');
			var objectType = getURLParameter('object');
			
 			var resources = [ {
				name : 'inventoryNode',
				uri : _BASE_WEB_ROOT + _CONF_SERVICES + '/node'
			},{
				name : 'slice',
				uri : _BASE_WEB_ROOT + _CONF_SERVICES + '/slice'
			}];
				
			Preloader.loadCustomResources(resources).then(function() {
				if (operation == 'edit') {
					console.log("user management edit ");
					updatePage(objectName);
				} else {
					WS_getUserList(showUserList, $('#user-management .box-body'));

					$('#user-add .btn-save').click(function() {
						WS_createUser(buildInput($('#user-add')), function(data) {
							WS_getUserList(showUserList, $('#user-management .box-body'));
						});
					});
				}
			});
		});
	</script>
</body>
</html>

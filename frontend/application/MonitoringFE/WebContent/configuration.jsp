<!DOCTYPE html>
<html>
<head>
<meta name="description" content="Monitoring WebGui">
<jsp:include page="include/header.jsp" />
<style>
</style>
</head>
<body
	class='skin-blue sidebar-mini <c:out value="${sessionScope.collapseClass}"/>'>
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
				<div class="row" id="main-container"></div>
			</section>
		</div>
		<jsp:include page="include/footer.jsp" />
		<jsp:include page="include/help-menu.jsp" />
	</div>
	<div class="modal fade" id="confirm-delete-dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header warning">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title">
						<span class="glyphicon glyphicon-warning-sign"> </span> Warning:
						are you sure you want to delete?
					</h4>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<button type="button" class="btn btn-primary btn-confirm">Confirm</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal" id="pleaseWaitDialog" data-backdrop="static"
		data-keyboard="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h1>Processing...</h1>
				</div>
				<div class="modal-body">
					<div class="progress progress-striped active">
						<div class="progress-bar progress-bar-primary" role="progressbar"
							aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"
							style="width: 100%;">
							<span class="sr-only">Processing...</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="include/scripts.jsp" />
	<script src="js/service.js"></script>
	<script src="js/node.js"></script>
	<script src="js/metric.js"></script>
	<script type="text/javascript">
	var operation;
	var objectType;
	var objectName;
	
	$(document).ready(function() {		
			objectName = getURLParameter('name');
			objectType = getURLParameter('object');
			operation = getURLParameter('operation');
			
			if ((objectType == 'inventoryService') || (objectType == 'Service')) {

				var resources = [ {
					name : 'inventoryNode',
					uri : _BASE_WEB_ROOT + _CONF_SERVICES + '/node'
				}, {
					name : 'inventoryMetric',
					uri : _BASE_WEB_ROOT + _CONF_SERVICES + '/metric'
				}, {
					name : 'inventoryService',
					uri : _BASE_WEB_ROOT + _CONF_SERVICES + '/service'
				}];
				Preloader.loadCustomResources(resources).then(function() {
					if (operation == 'read') {
						$.Service.readPage(objectName);
					} else if (operation == 'create') {
						$.Service.createPage();
					} else if (operation == 'edit') {
						$.Service.updatePage(objectName);
					} else if (operation == 'list') {
						$.Service.listPage();
					}
				});
			} else 	if ((objectType == 'inventoryNode') || (objectType == 'Node')){
				var resources = [ {
					name : 'inventoryNode',
					uri : _BASE_WEB_ROOT + _CONF_SERVICES + '/node'
				}, {
					name : 'inventoryService',
					uri : _BASE_WEB_ROOT + _CONF_SERVICES + '/service'
				}];
				Preloader.loadCustomResources(resources).then(function() {
					if (operation == 'read') {
						$.Node.readPage(objectName);
					} else if (operation == 'create') {
						$.Node.createPage();
					} else if (operation == 'list') {
						$.Node.listPage();
					}
				});
			} else if ((objectType == 'inventoryMetric') || (objectType == 'Metric')) {
				var resources = [{
					name : 'inventoryMetric',
					uri : _BASE_WEB_ROOT + _CONF_SERVICES + '/metric'
				}];
				Preloader.loadCustomResources(resources).then(function() {
					if (operation == 'read') {
						$.Metric.readPage(objectName);
					} else if (operation == 'create') {
						$.Metric.createPage();
					} else if (operation == 'list') {
						$.Metric.listPage();
					}
				});
			}
		});
		</script>
</body>
</html>

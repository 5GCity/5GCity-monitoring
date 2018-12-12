<script src="jslib/jquery-1.11.3.min.js"></script>
<script src="jslib/jquery-ui-1.10.3.min.js"></script>
<script src="jslib/jquery.browser.min.js"></script>
<script src="jslib/bootstrap-3.3.5/js/bootstrap.js"></script>
<script src="jslib/plugins/flot/excanvas.min.js"></script>
<script src="jslib/plugins/jqueryKnob/jquery.knob.js"></script>
<script src="jslib/plugins/daterangepicker/moment.js"></script>
<script src="jslib/plugins/daterangepicker/daterangepicker.new.js"></script>
<script src="jslib/plugins/input-mask/jquery.inputmask.js"></script>
<script src="jslib/plugins/input-mask/jquery.inputmask.extensions.js"></script>
<script src="jslib/plugins/jquery-cron/jquery-cron.js"></script>
<script
	src="jslib/plugins/jquery-textcomplete/jquery.textcomplete.min.js"></script>
<script
	src="jslib/plugins/jquery-highlighttextarea/jquery.highlighttextarea.js"></script>
<script src="jslib/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<script type="text/javascript"
	src="jslib/plugins/DataTables-1.10.9/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="jslib/plugins/DataTables-1.10.9/js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript"
	src="jslib/plugins/DataTables-1.10.9/Responsive-2.0.0/js/dataTables.responsive.min.js"></script>
<script type="text/javascript"
	src="jslib/plugins/DataTables-1.10.9/Responsive-2.0.0/js/responsive.bootstrap.min.js"></script>
<script src="jslib/networking.js"></script>
<script src="jslib/plugins/jsTree/jstree.min.js"></script>
<script src="jslib/plugins/jsonPath/jsonpath-0.8.0.js"></script>

<script src="js/app.js"></script>
<script src="js/common-html-utils.js"></script>
<script src="js/common.js"></script>
<script src="js/common-buttons.js"></script>
<script src="js/bootstrap-switch.js"></script>
<script>
	$(document).ready(function() {
		var url = window.location.pathname.toLowerCase();
		if(url.match(/\.jsp$/) != null && !(url.match(/index\.jsp$/) != null)) {
			console.log("fault error")
		}
	});
</script>
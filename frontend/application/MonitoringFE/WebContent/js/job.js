$.Job = {
	
	listPageLink : 'configuration.jsp?operation=list&object=job',

	listPage : function(successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Job.displayListPage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Job.WS.list(successHandler, errorHandler, targetDom);
	},

	createPage : function(successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Job.displayCreatePage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Job.WS.template(successHandler, errorHandler, targetDom);
	},

	updatePage : function(name, successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Job.displayUpdatePage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Job.WS.read(name, successHandler, errorHandler, targetDom);
	},

	readPage : function(name, successHandler, errorHandler, targetDom) {
		successHandler = successHandler
				|| $.Job.displayReadOnlyPage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Job.WS.read(name, successHandler, errorHandler, targetDom);
	},

	actionCreate : function() {
		$.Job.WS.create(buildInput($('#main-container')),$.Job.goToListPage);
	},

	actionUpdate : function() {
		$.Job.WS.update(buildInput($('#main-container')), $.Job.goToListPage);
	},

	actionRemove : function(name) {
		$.Job.WS.remove(name, $.Job.goToListPage);
	},

	goToListPage : function() {
		window.location.href = $.Job.listPageLink;
	},

	displayReadOnlyPage : function(data, targetDom) {
		var obj = data.data;

		listAllButton('#nav-toolbar', obj.name, 'Job');
		confirmDeleteButton('#nav-toolbar', obj.name,
					$.Job.actionRemove);
		
		setPageTitle('Jobs: '+ obj.name);
		setActiveMenu('menu_Job');

		$.Job.displayFormData(obj, targetDom, true, true);
	},

	displayCreatePage : function(data, targetDom) {
		var obj = data.data;

		setPageTitle("Create Job");
		setActiveMenu('menu_job');

		saveButton('#nav-toolbar', $.Job.actionCreate);
		cancelButton('#nav-toolbar', $.Job.listPageLink);
		
		$.Job.displayFormData(obj, targetDom);
	},

	displayUpdatePage : function(data, targetDom) {
		var obj = data.data;

		saveButton('#nav-toolbar', $.Job.actionUpdate, obj.name);
		cancelButton('#nav-toolbar', $.Job.listPageLink);

		setPageTitle("Update Job: " + obj.name);
		setActiveMenu('menu_job');

		$.Job.displayFormData(obj, targetDom, true);
	},

	displayFormData : function(job, targetDom, update, readonly) {
		update = update || false;
		readonly = readonly || false;
		var content = '';
		content += '<form role="form">';

		var display = '';
		if (update && !readonly) {
			display = 'style="display:none;"';
		}

		content += '<div class="col-md-3">';
		var boxgeneral = '';
		boxgeneral += '<div class="form-group data-input-l1" ' + display
				+ '><label>Name</label>';
		boxgeneral += inputText('name', job.name);
		boxgeneral += '</div>';
		boxgeneral += '<div class="form-group data-input-l1" ><label>Description</label>';
		boxgeneral += inputText('description', job.description);
		boxgeneral += '</div>';
		
		content += condensedBoxPrimary('Settings', boxgeneral);
		
		content += '</div>';
		
		content += '<div class="col-md-4">';
		
		var metricPath = null;
		var metricPath_display = "style=\"display:none;\"";
		var port = 9100;
		var interval = 30;
		var path = "/metrics";
		var sourceType = "EXPORTER";
		var dashboardType = "NODE";
		
		if (!isNull(job.jobSource)) {
			metricPath = job.jobSource.metricPath;
			port = job.jobSource.port;
			interval = job.jobSource.interval;
			sourceType = job.jobSource.sourceType;
			dashboardType = job.jobSource.dashboardType;
		}
		var dashboardTypeList = [];
		$.Job.WS.getCachedDashboardTypeList().each(function() {
			var sn = this.type;

			dashboardTypeList.push({
				'value' : this.type,
				'label' : sn
			});
		});		

		var boxsource = '';
		boxsource += '<div id="edit-job-source" class="data-input-l1" data-inputname="jobSource">';
		boxsource += '<div id="edit-source-type" class="form-group data-input-l2"><label>Source Type</label>';
		boxsource += dynamicSelect('sourceType', sourceType, sourceTypes);
		boxsource += '</div>';
		boxsource += '<div id="edit-metric-path" class="form-group data-input-l2""><label>Metric Path</label>';
		boxsource += inputText('metricPath', metricPath);
		boxsource += '</div>';
		boxsource += '<div id="edit-source-port" class="form-group data-input-l2""><label>Port</label>';
		boxsource += inputNumber('port', port);
		boxsource += '</div>';
		boxsource += '<div id="edit-source-interval" class="form-group data-input-l2""><label>Interval</label>';
		boxsource += inputNumber('interval', interval);
		boxsource += '</div>';
		boxsource += '<div id="edit-dashboard-type" class="form-group data-input-l2""><label>Dashboard Type</label>';
		boxsource += dynamicSelect('dashboardType', dashboardType, dashboardTypeList);
		boxsource += '</div>';

		boxsource += '</div>';
		
		content += condensedBoxPrimary('Source Settings', boxsource);
		content += '</div>';

		var boxNodeList = '';
		boxNodeList += '<div class="form-group data-input-array data-input-l1" data-inputname="nodes">';
		boxNodeList += '<div id="edit-node-list" class="inline-condensed"></div>';
		boxNodeList += '</div>';
		
		content += '<div class="col-md-4">';
		content += condensedBoxPrimary('Node List', boxNodeList);
		content += '</div>';
		content += '</form>';

		$(targetDom).html(content);
		
		$.Job.adjustNodeList(job, readonly);
		nodeListChange(job);
		
		var inListObj = {};
		var nodes = Preloader.getResource('inventoryNode').nodes;
		$(nodes).each(function(){
			var name = this.name;
			if(isNull(inListObj[name]))
				inListObj[name] = this;
		});
		
		$('#edit-node-list select').change();		
		
		$('#edit-dashboard-type select').change(function() {
			var dashType = $('#edit-dashboard-type select').find('option:selected').attr('value');			
			var portType = $('input[name="port"]').val();

			if ((dashType == 'NODE') && (portType == 9117)) {
				//$('input[name="port"').html(inputNumber('port', '9100'));
				$('input[name="port"]').val('9100');
			} else if ((dashType == 'APACHE') && (portType == 9100)) {
				//$('input[name="port"]').html(inputNumber('port', '9117'));
				$('input[name="port"]').val('9117');
			}
		});
		
		if (readonly) {
			setReadOnlyForm($(targetDom));
		}
		
		$.Job.help();
	},

	help : function() {

	},

	displayListPage : function(data, targetDom) {
//		console.log("displayListPage data :");
//		console.log(data);
		var jobs = data.data.jobs;
		
		var count = $(jobs).length;
		createButton('job', '#nav-toolbar');
		
		setPageTitle('Jobs');
		setActiveMenu('menu_job');

		var boxcontent = '';
		var content = '<div class="col-md-12">';

		if (count == 0) {
			boxcontent += '<div data-help-label="" data-help-key="jobsList" />';
			boxcontent += '<p>No Job found</p>';
		} else {
			boxcontent += '<div data-help-label="Name" data-help-key="name" />';
			boxcontent += '<div data-help-label="Description" data-help-key="description" />';
			boxcontent += '<div data-help-label="Actions" data-help-key="actions" />';
			boxcontent += '<table class="table table-hover table-striped"><thead><tr><th class="col-md-3">Name</th><th class="col-md-10">Description</th><th class="col-md-3">Actions</th></tr></thead><tboby>';

			$(jobs).each(function() {
					
					boxcontent += '<tr><td>' + this.name + '</td><td>' + this.description + '</td>';

					boxcontent += '<td><div class="inline-toolbar" data-target-name="' + this.name
								+ '" data-target-object="job" ></div></td></tr>';
					
					});
			boxcontent += '</tbody><tfoot></tfoot></table>';
		}

		content += condensedBoxPrimary('Job <span class="badge">'
				+ count + '</span>', boxcontent);
		content += '</div>';

		$(targetDom).html(content);

//		inlineDashboardDetailButton('.inline-toolbar','/dashboard/db/detailsnode?refresh=1h&orgId=1&var-instance=');
		inlineConfigViewButton('.inline-toolbar');
		inlineConfigUpdateButton('.inline-toolbar');
		inlineConfirmDeleteButton('.inline-toolbar', targetDom,
					$.Job.actionRemove);

		$(targetDom).find('table').DataTable({
			"responsive" : true,
			"columnDefs" : [ {
				responsivePriority : 1,
				targets : 0
			}, {
				orderable : false,
				responsivePriority : 2,
				targets : -1
			} ],
			"order" : [ [ 0, "asc" ] ]
		});

		$.Job.help();
	},

	adjustNodeList : function(job, readonly) {
		readonly = readonly || false;
		var content = '';
		if (!readonly) {
			if($(job.nodes).length > 0) {
				$(job.nodes).each(function(indexInArray, value) {
					content += removableNodeName(job,value.name, 'data-input-l2');
				});
			} else {
				content += removableNodeName(job, null, 'data-input-l2');
			}
			content += '<button type="button" class="btn btn-default btn-sm btn-block btn-add"><span class="glyphicon glyphicon-plus"></span> Add</button>';
		} else {
			$(job.nodes).each(function(indexInArray, value) {
				content += removableNodeName(job, value.name, 'data-input-l2', readonly);
			});
		}
		$('#edit-node-list').html(content);

		if (!readonly) {
			$('#edit-node-list').find('button.btn-add').click(function() {
				
				$(this).before('' + removableNodeName(job, null, 'data-input-l2'));
				nodeListChange(job);
				
				var selectList = $('#edit-node-list select');
				var lastSelectAdded = selectList[selectList.length-1];
				$(lastSelectAdded).change(function(){
					$.lastSelectChanged = this;
				});
				$(lastSelectAdded).change();
			});
		}
	},

};

$.Job.WS = {

	create : function(inputData, successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		postValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/job',
				inputData, successHandler, errorHandler, targetDom,
				'modal-body');
	},

	update : function(inputData, successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		putValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/job',
				inputData, successHandler, errorHandler, targetDom,
				'modal-body');
	},
	
	list : function(successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		
		getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/job',
				successHandler, errorHandler, targetDom);
	},

	remove : function(name, successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		deleteValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/job/'
				+ name, successHandler, errorHandler, targetDom, 'modal-body');
	},

	read : function(name, successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/job/' + name,
				successHandler, errorHandler, targetDom);
	},

	template : function(successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/job/template',
				successHandler, errorHandler, targetDom);
	},

	getCachedInventoryNodeList : function() {
		var cacheNode = Preloader.getResource('inventoryNode').inventoryNodes;
		return $(cacheNode);
	},

	getCachedDashboardTypeList : function() {
		var cacheDashboard = Preloader.getResource('dashboardType').dashboardTypes;
		return $(cacheDashboard);
	},
};

function nodeListChange(job) {
	
	var totNode = $('#edit-node-list').find('div.row').length;
		
	if (totNode <= 1) {
	
		var btn = $('#edit-node-list').find('span.glyphicon-minus').parent('button.btn');
		btn[0].disabled = true;
	}
	
	$.Job.help();
};


function removableNodeName(job, nodeName, inputclass, readonly, hideLabels) {
	readonly = readonly || false;
	var nodeNamesList = [];
	$.Job.WS.getCachedInventoryNodeList().each(function() {
		var sn = this.name;

		nodeNamesList.push({
			'value' : this.name,
			'label' : sn
		});
	});
	nodeName = nodeName || "";

	var html = '';
	html += '<div class="row ' + inputclass + '">';
	html += '<div class="col-xs-6">';
	
	html += '<label>Name</label>';
	html += dynamicSelect('name', nodeName, nodeNamesList);
	html += '</div>';

	html += '<div class="col-xs-2">';
	html += '<label>&nbsp;</label>';
	
	$.serv = job;
	html += '<div><button onclick="javascript: \
						if($(\'#edit-node-list\').find(\'span.glyphicon-minus\').length > 1)\
						{$(this).parent().parent().parent().remove();nodeListChange($.serv);}"\
			 class="btn btn-default btn-flat btn-sm pull-right" type="button"><span class="glyphicon glyphicon-minus"></span></button></div>';
	html += '</div>';
	html += '</div>';
	return html;
};



$.Service = {
		
	listPageLink : 'configuration.jsp?operation=list&object=inventoryService',

	listPage : function(successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Service.displayListPage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Service.WS.list(successHandler, errorHandler, targetDom);
	},

	createPage : function(successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Service.displayCreatePage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Service.WS.template(successHandler, errorHandler, targetDom);
	},

	updatePage : function(name, successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Service.displayUpdatePage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Service.WS.read(name, successHandler, errorHandler, targetDom);
	},

	readPage : function(name, successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Service.displayReadOnlyPage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Service.WS.read(name, successHandler, errorHandler, targetDom);
	},

	actionCreate : function() {
		$.Service.WS.create(buildInput($('#main-container')), $.Service.goToListPage);
	},

	actionUpdate : function() {
		$.Service.WS.update(buildInput($('#main-container')), $.Service.goToListPage);
	},

	actionRemove : function(name) {
		$.Service.WS.remove(name, $.Service.goToListPage);
	},

	goToListPage : function() {
		window.location.href = $.Service.listPageLink;
	},

	displayReadOnlyPage : function(data, targetDom) {
		var service = data.data;

		listAllButton('#nav-toolbar', service.name, 'Service');

		configUpdateButton('#nav-toolbar', service.name, 'Service');
		confirmDeleteButton('#nav-toolbar', service.name,
					$.Service.actionRemove);
			
		setPageTitle('Services: '+ service.name);
		setActiveMenu('menu_service');

		$.Service.displayFormData(service, targetDom, true, true);
	},

	displayCreatePage : function(data, targetDom) {
		var service = data.data;

		setPageTitle("Create Service");
		setActiveMenu('menu_service');

		saveButton('#nav-toolbar', $.Service.actionCreate);
		cancelButton('#nav-toolbar', $.Service.listPageLink);
		$.Service.displayFormData(service, targetDom);
		
	},

	displayUpdatePage : function(data, targetDom) {
		var service = data.data;

		saveButton('#nav-toolbar', $.Service.actionUpdate, service.name);
		cancelButton('#nav-toolbar', $.Service.listPageLink);

		setPageTitle("Update Service: " + service.name);
		setActiveMenu('menu_service');

		$.Service.displayFormData(service, targetDom, true);
	},

	displayFormData : function(service, targetDom, update, readonly) {
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
		boxgeneral += inputText('name', service.name);
		boxgeneral += '</div>';
		boxgeneral += '<div class="form-group data-input-l1" ><label>Description</label>';
		boxgeneral += inputText('description', service.description);
		boxgeneral += '</div>';
		boxgeneral += '<div id="edit-interval" class="form-group data-input-l1"><label>Interval</label>';
		boxgeneral += inputNumber('interval', service.interval);
		boxgeneral += '</div>';

		content += condensedBoxPrimary('Settings', boxgeneral);

		content += '</div>';
		
		var boxNodeList = '';
		boxNodeList += '<div class="form-group data-input-array data-input-l1" data-inputname="nodes">';
		boxNodeList += '<div id="edit-node-list" class="inline-condensed"></div>';
		boxNodeList += '</div>';
		
		content += '<div class="col-md-6">';
		content += condensedBoxPrimary('Node List', boxNodeList);
		content += '</div>';
		
		var boxMetricList = '';
		boxMetricList += '<div class="form-group data-input-array data-input-l1" data-inputname="metrics">';
		boxMetricList += '<div id="edit-metric-list" class="inline-condensed"></div>';
		boxMetricList += '</div>';
		
		content += '<div class="col-md-6">';
		content += condensedBoxPrimary('Metric List', boxMetricList);
		content += '</div>';		
		content += '</form>';

		$(targetDom).html(content);
		
		$.Service.adjustNodeList(service, readonly);
		nodeListChange(service);
		
		var inListObj = {};
		var nodes = Preloader.getResource('inventoryNode').nodes;
		console.log("dopo preloader nodes :",nodes);
		$(nodes).each(function(){
			var name = this.name;
			if(isNull(inListObj[name]))
				inListObj[name] = this;
		});
		
		$('#edit-node-list select').change();		
		
		// METRIC
		$.Service.adjustMetricList(service, readonly);
		metricListChange(service);
		
		var inListObj = {};
		var metrics = Preloader.getResource('inventoryMetric').metrics;
		console.log("dopo preloader metrics :",metrics);
		$(metrics).each(function(){
			var name = this.name;
			if(isNull(inListObj[name]))
				inListObj[name] = this;
		});
		
		$('#edit-metric-list select').change();
		
		if (readonly) {
			setReadOnlyForm($(targetDom));
		}
		
		$.Service.help();
	},
	

	help : function() {

	},
	
	displayListPage : function(data, targetDom) {

		var inventoryServices = data.data.inventoryServices;
		
		var count = $(inventoryServices).length;

		createButton('inventoryService', '#nav-toolbar');
		
		setPageTitle('Services');
		setActiveMenu('menu_service');

		var boxcontent = '';
		var content = '<div class="col-md-12">';

		if (count == 0) {
			boxcontent += '<div data-help-label="" data-help-key="servicesList" />';
			boxcontent += '<p>No Service found</p>';
		} else {
			boxcontent += '<div data-help-label="Name" data-help-key="name" />';
			boxcontent += '<div data-help-label="Description" data-help-key="description" />';
			boxcontent += '<div data-help-label="Interval" data-help-key="interval" />';
			boxcontent += '<div data-help-label="Actions" data-help-key="actions" />';
			boxcontent += '<table class="table table-hover table-striped"><thead><tr><th class="col-md-3">Name</th><th class="col-md-10">Description</th><th class="col-md-3">Interval</th><th class="col-md-3">Actions</th></tr></thead><tboby>';
			$(inventoryServices).each(
					function() {
						
					boxcontent += '<tr><td>' + this.name + '</td><td>' + this.description + '</td><td>' + this.interval + '</td>';
						
					boxcontent += '<td><div class="inline-toolbar" data-target-name="' + this.name
								+ '" data-target-object="inventoryService"></div></td></tr>';
					});
			boxcontent += '</tbody><tfoot></tfoot></table>';
		}

		content += condensedBoxPrimary('Service <span class="badge">'
				+ count + '</span>', boxcontent);
		content += '</div>';

		$(targetDom).html(content);

		inlineConfigViewButton('.inline-toolbar');
		inlineConfigUpdateButton('.inline-toolbar');
		inlineConfirmDeleteButton('.inline-toolbar', targetDom,
					$.Service.actionRemove);
		
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

		$.Service.help();
	},

	adjustNodeList : function(service, readonly) {
		readonly = readonly || false;
		var content = '';
		if (!readonly) {
			if($(service.nodes).length > 0) {
				$(service.nodes).each(function(indexInArray, value) {
					content += removableNodeName(service,value.name, 'data-input-l2');
				});
			} else {
				content += removableNodeName(service, null, 'data-input-l2');
			}
			content += '<button type="button" class="btn btn-default btn-sm btn-block btn-add"><span class="glyphicon glyphicon-plus"></span> Add</button>';
		} else {
			$(service.nodes).each(function(indexInArray, value) {
				content += removableNodeName(service, value.name, 'data-input-l2', readonly);
			});
		}
		$('#edit-node-list').html(content);

		if (!readonly) {
			$('#edit-node-list').find('button.btn-add').click(function() {
				
				$(this).before('' + removableNodeName(service, null, 'data-input-l2'));
				nodeListChange(service);
				
				var selectList = $('#edit-node-list select');
				var lastSelectAdded = selectList[selectList.length-1];
				$(lastSelectAdded).change(function(){
					$.lastSelectChanged = this;
				});
				$(lastSelectAdded).change();
			});
		}
	},

	adjustMetricList : function(service, readonly) {
		readonly = readonly || false;
		var content = '';
		if (!readonly) {
			if($(service.metrics).length > 0) {
				$(service.metrics).each(function(indexInArray, value) {
					content += removableMetricName(service,value.name, 'data-input-l2');
				});
			} else {
				content += removableMetricName(service, null, 'data-input-l2');
			}
			content += '<button type="button" class="btn btn-default btn-sm btn-block btn-add"><span class="glyphicon glyphicon-plus"></span> Add</button>';
		} else {
			$(service.metrics).each(function(indexInArray, value) {
				content += removableMetricName(service, value.name, 'data-input-l2', readonly);
			});
		}
		$('#edit-metric-list').html(content);

		if (!readonly) {
			$('#edit-metric-list').find('button.btn-add').click(function() {
				
				$(this).before('' + removableMetricName(service, null, 'data-input-l2'));
				metricListChange(service);
				
				var selectList = $('#edit-metric-list select');
				var lastSelectAdded = selectList[selectList.length-1];
				$(lastSelectAdded).change(function(){
					$.lastSelectChanged = this;
				});
				$(lastSelectAdded).change();
			});
		}
	},
};

$.Service.WS = {

		update : function(inputData, successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			postValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/service',
					inputData, successHandler, errorHandler, targetDom,
					'modal-body');
		},

		create : function(inputData, successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			putValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/service',
					inputData, successHandler, errorHandler, targetDom,
					'modal-body');
		},

		list : function(successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			
			getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/service',
					successHandler, errorHandler, targetDom);
		},

		remove : function(name, successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			deleteValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/service/'
					+ name, successHandler, errorHandler, targetDom, 'modal-body');
		},

		read : function(name, successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/service/' + name,
					successHandler, errorHandler, targetDom);
		},

		template : function(successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/service/template',
					successHandler, errorHandler, targetDom);
		},

		getCachedInventoryNodeList : function() {
			var cacheNode = Preloader.getResource('inventoryNode').inventoryNodes;
			return $(cacheNode);
		},
		
		getCachedInventoryMetricList : function() {
			var cacheMetric = Preloader.getResource('inventoryMetric').inventoryMetrics;
			return $(cacheMetric);
		}
};

function metricListChange(service) {
	
	var totMetric = $('#edit-metric-list').find('div.row').length;
	console.log("totMetric : ",totMetric);
		
	if (totMetric <= 1) {
	
		var btn = $('#edit-metric-list').find('span.glyphicon-minus').parent('button.btn');
		console.log("btn : ",btn);
		btn[0].disabled = true;
	}
	
	$.Service.help();
};

function nodeListChange(service) {
	
	var totNode = $('#edit-node-list').find('div.row').length;
	console.log("totNode : ",totNode);
		
	if (totNode <= 1) {
	
		var btn = $('#edit-node-list').find('span.glyphicon-minus').parent('button.btn');
		console.log("btn : ",btn);
		btn[0].disabled = true;
	}
	
	$.Service.help();
};


function removableNodeName(service, nodeName, inputclass, readonly, hideLabels) {
	readonly = readonly || false;
	var nodeNamesList = [];
	$.Service.WS.getCachedInventoryNodeList().each(function() {
		var sn = this.name + ' - ' + this.ip + ':' + this.port;

		nodeNamesList.push({
			'value' : this.name,
			'label' : sn
		});
	});
	nodeName = nodeName || "";
	console.log("nodeNamesList : ",nodeNamesList);

	var html = '';
	html += '<div class="row ' + inputclass + '">';
	html += '<div class="col-xs-6">';
	
	html += '<label>Name</label>';
	html += dynamicSelect('name', nodeName, nodeNamesList);
	html += '</div>';

	html += '<div class="col-xs-2">';
	html += '<label>&nbsp;</label>';
	
	$.serv = service;
	console.log("serv : ",service);
	html += '<div><button onclick="javascript: \
						if($(\'#edit-node-list\').find(\'span.glyphicon-minus\').length > 1)\
						{$(this).parent().parent().parent().remove();nodeListChange($.serv);}"\
			 class="btn btn-default btn-flat btn-sm pull-right" type="button"><span class="glyphicon glyphicon-minus"></span></button></div>';
	html += '</div>';
	html += '</div>';
	return html;
};

function removableMetricName(service, metricName, inputclass, readonly, hideLabels) {
	readonly = readonly || false;
	var metricNamesList = [];
	$.Service.WS.getCachedInventoryMetricList().each(function() {
		
		var sm = this.name;
		
		metricNamesList.push({
			'value' : this.name,
			'label' : sm
		});
	});
	metricName = metricName || "";
	console.log("metricNamesList : ",metricNamesList);
	console.log("inputclass : ",inputclass);
	var html = '';
	html += '<div class="row ' + inputclass + '">';
	html += '<div class="col-xs-6">';
	
	html += '<label>Name</label>';
	html += dynamicSelect('name', metricName, metricNamesList);
	html += '</div>';

	html += '<div class="col-xs-2">';
	html += '<label>&nbsp;</label>';
	
	$.serv = service;
	console.log("serv : ",service);
	html += '<div><button onclick="javascript: \
						if($(\'#edit-metric-list\').find(\'span.glyphicon-minus\').length > 1)\
						{$(this).parent().parent().parent().remove();metricListChange($.serv);}"\
			 class="btn btn-default btn-flat btn-sm pull-right" type="button"><span class="glyphicon glyphicon-minus"></span></button></div>';
	html += '</div>';
	html += '</div>';
	return html;
};
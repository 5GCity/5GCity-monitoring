var metricEnumTypes = [ {
	'value' : 'NODE',
	'label' : 'NODE',
	'type' : 'DEFAULT'
}];

$.Metric = {
	
	listPageLink : 'configuration.jsp?operation=list&object=inventoryMetric',

	listPage : function(successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Metric.displayListPage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Metric.WS.list(successHandler, errorHandler, targetDom);
	},

	createPage : function(successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Metric.displayCreatePage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Metric.WS.template(successHandler, errorHandler, targetDom);
	},

	readPage : function(name, successHandler, errorHandler, targetDom) {
		successHandler = successHandler
				|| $.Metric.displayReadOnlyPage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Metric.WS
				.read(name, successHandler, errorHandler, targetDom);
	},

	actionCreate : function() {
		$.Metric.WS.create(buildInput($('#main-container')),
				$.Metric.goToListPage);
	},

	actionRemove : function(name) {
		$.Metric.WS.remove(name, $.Metric.goToListPage);
	},

	goToListPage : function() {
		window.location.href = $.Metric.listPageLink;
	},

	displayReadOnlyPage : function(data, targetDom) {
		var obj = data.data;

		listAllButton('#nav-toolbar', obj.name, 'Metric');

		confirmDeleteButton('#nav-toolbar', obj.name,
					$.Metric.actionRemove);
		
		setPageTitle('Inventory Metrics: '+ obj.name);
		setActiveMenu('menu_metric');

		$.Metric.displayFormData(obj, targetDom, true, true);
	},

	displayCreatePage : function(data, targetDom) {

		var obj = data.data;

		setPageTitle("Create Metric");
		setActiveMenu('menu_metric');

		saveButton('#nav-toolbar', $.Metric.actionCreate);
		cancelButton('#nav-toolbar', $.Metric.listPageLink);

		$.Metric.displayFormData(obj, targetDom);		
	},

	displayFormData : function(inventoryMetric, targetDom, update, readonly) {
		update = update || false;
		readonly = readonly || false;
		var content = '';
		content += '<form role="form">';

		var display = '';
		if (update && !readonly) {
			display = 'style="display:none;"';
		}

		content += '<div class="col-md-4">';
		var boxgeneral = '';
		
		boxgeneral += '<div id="metric-name" class="form-group data-input-l1"><label>Name</label>';
		boxgeneral += dynamicSelect('name',inventoryMetric.name,
				metricEnumTypes);
		boxgeneral += '</div>';
		boxgeneral += '<div class="form-group data-input-l1" ><label>Description</label>';
		boxgeneral += inputText('desc', inventoryMetric.desc);
		boxgeneral += '</div>';

		content += condensedBoxPrimary('Settings', boxgeneral);

		content += '</form>';

		$(targetDom).html(content);

		if (readonly) {
			setReadOnlyForm($(targetDom));
		}

		$.Metric.help();
	},

	help : function() {

	},

	displayListPage : function(data, targetDom) {

		var inventoryMetrics = data.data.inventoryMetrics;
		
		var count = $(inventoryMetrics).length;

		createButton('inventoryMetric', '#nav-toolbar');
		
		setPageTitle('Inventory Metrics');
		setActiveMenu('menu_metric');

		var boxcontent = '';
		var content = '<div class="col-md-12">';

		if (count == 0) {
			boxcontent += '<div data-help-label="" data-help-key="MetricsList" />';
			boxcontent += '<p>No Metric found</p>';
		} else {
			boxcontent += '<div data-help-label="Name" data-help-key="name" />';
			boxcontent += '<div data-help-label="Description" data-help-key="desc" />';
			boxcontent += '<div data-help-label="Actions" data-help-key="actions" />';
			boxcontent += '<table class="table table-hover table-striped"><thead><tr><th class="col-md-3">Name</th><th class="col-md-10">Description</th><th class="col-md-3">Actions</th></tr></thead><tboby>';
			$(inventoryMetrics).each(
					function() {
						
					boxcontent += '<tr><td>' + this.name + '</td><td>' + this.desc + '</td>';
						
					boxcontent += '<td><div class="inline-toolbar" data-target-name="' + this.name
								+ '" data-target-object="inventoryMetric"></div></td></tr>';
					});
			boxcontent += '</tbody><tfoot></tfoot></table>';
		}

		content += condensedBoxPrimary('Metric <span class="badge">'
				+ count + '</span>', boxcontent);
		content += '</div>';

		$(targetDom).html(content);

		inlineConfigViewButton('.inline-toolbar');
		inlineConfirmDeleteButton('.inline-toolbar', targetDom,
					$.Metric.actionRemove);		

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

		$.Metric.help();
	},

};

$.Metric.WS = {

	create : function(inputData, successHandler, errorHandler, targetDom) {
		console.log("CREATE");
		errorHandler = errorHandler || null;
		putValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/inventoryMetric',
				inputData, successHandler, errorHandler, targetDom,
				'modal-body');
	},

	list : function(successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		
		getValues('/FrontEnd/rest/fe/inventoryMetric',
				successHandler, errorHandler, targetDom);
	},

	remove : function(name, successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		deleteValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/inventoryMetric/'
				+ name, successHandler, errorHandler, targetDom, 'modal-body');
	},

	read : function(name, successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/inventoryMetric/' + name,
				successHandler, errorHandler, targetDom);
	},

	template : function(successHandler, errorHandler, targetDom) {
		console.log("TEMPLATE");
		errorHandler = errorHandler || null;
		getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/inventoryMetric/template',
				successHandler, errorHandler, targetDom);
	},

};




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

		content += condensedBoxPrimary('Settings', boxgeneral);

		content += '</div>';
		
		var boxJobList = '';
		boxJobList += '<div class="form-group data-input-array data-input-l1" data-inputname="jobs">';
		boxJobList += '<div id="edit-job-list" class="inline-condensed"></div>';
		boxJobList += '</div>';
		
		content += '<div class="col-md-4">';
		content += condensedBoxPrimary('Job List', boxJobList);
		content += '</div>';
		
		content += '</form>';

		$(targetDom).html(content);
		
		$.Service.adjustJobList(service, readonly);
		jobListChange(service);
		
		var inListObj = {};
		var jobs = Preloader.getResource('job').jobs;
		$(jobs).each(function(){
			var name = this.name;
			if(isNull(inListObj[name]))
				inListObj[name] = this;
		});
		
		$('#edit-job-list select').change();		
				
		if (readonly) {
			setReadOnlyForm($(targetDom));
		}
		
		$.Service.help();
	},
	

	help : function() {

	},
	
	getDashTypeByJob : function(myjob) { 
		var jobs = Preloader.getResource('job').jobs;
		var type = "";
		$(jobs).each(
				function() {
					if(this.name == myjob) {
						type = this.jobSource.dashboardType;			
					}
				});	
		return type;
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
			boxcontent += '<div data-help-label="Actions" data-help-key="actions" />';
			boxcontent += '<table class="table table-hover table-striped"><thead><tr><th class="col-md-3">Name</th><th class="col-md-10">Description</th><th class="col-md-3">Actions</th></tr></thead><tboby>';
			
			$(inventoryServices).each(
					function() {
						boxcontent += '<tr><td>' + this.name + '</td><td>' + this.description + '</td>';
						var myjobs = this.jobs;
						var jobDashTypeList = [];
						$(myjobs).each(
								function() {
									var jobInservice = this.name;
									$.Service.WS.getCachedJobList().each(function() {
										if(this.name == jobInservice) {
											var sn = this.jobSource.dashboardType;
											if(jobDashTypeList.indexOf(sn) == -1)
											   jobDashTypeList.push(sn);
										}
									});
								});
						console.log("jobDashTypeList :",jobDashTypeList);
					boxcontent += '<td><div class="inline-toolbar" data-target-name="' + this.name
					+ '" data-target-object="Service" data-dash-type="' + jobDashTypeList +'" ></div></td></tr>';
					});
			boxcontent += '</tbody><tfoot></tfoot></table>';
		}
		content += condensedBoxPrimary('Service <span class="badge">'
				+ count + '</span>', boxcontent);
		content += '</div>';

		$(targetDom).html(content);

		inlineDashboardGenericButton('.inline-toolbar');
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

	adjustJobList : function(service, readonly) {
		readonly = readonly || false;
		var content = '';
		if (!readonly) {
			if($(service.jobs).length > 0) {
				$(service.jobs).each(function(indexInArray, value) {
					content += removableJobName(service,value.name, 'data-input-l2');
				});
			} else {
				content += removableJobName(service, null, 'data-input-l2');
			}
			content += '<button type="button" class="btn btn-default btn-sm btn-block btn-add"><span class="glyphicon glyphicon-plus"></span> Add</button>';
		} else {
			$(service.jobs).each(function(indexInArray, value) {
				content += removableJobName(service, value.name, 'data-input-l2', readonly);
			});
		}
		$('#edit-job-list').html(content);

		if (!readonly) {
			$('#edit-job-list').find('button.btn-add').click(function() {
				
				$(this).before('' + removableJobName(service, null, 'data-input-l2'));
				jobListChange(service);
				
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

		getCachedJobList : function() {
			var cacheJob = Preloader.getResource('job').jobs;
			return $(cacheJob);
		},
		
};

function jobListChange(service) {
	
	var totJob = $('#edit-job-list').find('div.row').length;
		
	if (totJob <= 1) {
	
		var btn = $('#edit-job-list').find('span.glyphicon-minus').parent('button.btn');
		btn[0].disabled = true;
	}
	
	$.Service.help();
};


function removableJobName(service, jobName, inputclass, readonly, hideLabels) {
	readonly = readonly || false;
	var jobNamesList = [];
	var jobTypeList = [];
	$.Service.WS.getCachedJobList().each(function() {
			var sn = this.name;

			jobNamesList.push({
				'value' : this.name,
				'label' : sn
			});
			var jb = this.name;
			jobTypeList.push({
				'value' : this.jobSource.dashboardType,
				'label' : jb
			});
	});
	jobName = jobName || "";

	var html = '';
	html += '<div class="row ' + inputclass + '">';
	html += '<div class="col-xs-10">';
	
	html += '<label>Name</label>';
	html += dynamicSelect('name', jobName, jobNamesList);
	html += '</div>';

	html += '<div class="col-xs-2">';
	html += '<label>&nbsp;</label>';
	
	$.serv = service;
	html += '<div><button onclick="javascript: \
						if($(\'#edit-job-list\').find(\'span.glyphicon-minus\').length > 1)\
						{$(this).parent().parent().parent().remove();jobListChange($.serv);}"\
			 class="btn btn-default btn-flat btn-sm pull-right" type="button"><span class="glyphicon glyphicon-minus"></span></button></div>';

	html += '</div>';
	html += '</div>';
	return html;
};

$.Slice = {
		
	listPageLink : 'configuration.jsp?operation=list&object=slice',

	listPage : function(successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Slice.displayListPage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Slice.WS.list(successHandler, errorHandler, targetDom);
	},

	createPage : function(successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Slice.displayCreatePage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Slice.WS.template(successHandler, errorHandler, targetDom);
	},

	updatePage : function(name, successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Slice.displayUpdatePage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Slice.WS.read(name, successHandler, errorHandler, targetDom);
	},

	readPage : function(name, successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Slice.displayReadOnlyPage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Slice.WS.read(name, successHandler, errorHandler, targetDom);
	},

	actionCreate : function() {
		$.Slice.WS.create(buildInput($('#main-container')), $.Slice.goToListPage);
	},

	actionUpdate : function() {
		console.log("actionUpdate");
		$.Slice.WS.update(buildInput($('#main-container')), $.Slice.goToListPage);
	},

	actionRemove : function(name) {
		$.Slice.WS.remove(name, $.Slice.goToListPage);
	},

	goToListPage : function() {
		window.location.href = $.Slice.listPageLink;
	},

	displayReadOnlyPage : function(data, targetDom) {
		var slice = data.data;

		listAllButton('#nav-toolbar', slice.name, 'Slice');

		configUpdateButton('#nav-toolbar', slice.name, 'Slice');
		confirmDeleteButton('#nav-toolbar', slice.name,
					$.Slice.actionRemove);
			
		setPageTitle('Slices: '+ slice.name);
		setActiveMenu('menu_slice');

		$.Slice.displayFormData(slice, targetDom, true, true);
	},

	displayCreatePage : function(data, targetDom) {
		var slice = data.data;

		setPageTitle("Create Slice");
		setActiveMenu('menu_slice');

		saveButton('#nav-toolbar', $.Slice.actionCreate);
		cancelButton('#nav-toolbar', $.Slice.listPageLink);
		$.Slice.displayFormData(slice, targetDom);
		
	},

	displayUpdatePage : function(data, targetDom) {
		var slice = data.data;

		saveButton('#nav-toolbar', $.Slice.actionUpdate, slice.name);
		cancelButton('#nav-toolbar', $.Slice.listPageLink);

		setPageTitle("Update Slice: " + slice.name);
		setActiveMenu('menu_slice');

		$.Slice.displayFormData(slice, targetDom, true);
	},

	displayFormData : function(slice, targetDom, update, readonly) {
		update = update || false;
		readonly = readonly || false;
		var content = '';
		content += '<form role="form">';

		var display = '';
		if (update && !readonly) {
			display = 'style="display:none;"';
		}
		console.log("displayFormData display : ",display);
		content += '<div class="col-md-3">';

		var boxgeneral = '';
		boxgeneral += '<div class="form-group data-input-l1" ' + display
				+ '><label>Name</label>';
		boxgeneral += inputText('name', slice.name);
		boxgeneral += '</div>';

		content += condensedBoxPrimary('Settings', boxgeneral);

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
		
		$.Slice.adjustNodeList(slice, readonly);
		nodeListChange(slice);
		
		var inListObj = {};
		var nodes = Preloader.getResource('inventoryNode').nodes;
		$(nodes).each(function(){
			var name = this.name;
			if(isNull(inListObj[name]))
				inListObj[name] = this;
		});
		
		$('#edit-node-list select').change();		
		
		if (readonly) {
			setReadOnlyForm($(targetDom));
		}
		
		$.Slice.help();
	},
	

	help : function() {

	},
	
	displayListPage : function(data, targetDom) {

		var slices = data.data.slices;
		
		var count = $(slices).length;

		createButton('slice', '#nav-toolbar');
		
		setPageTitle('Slices');
		setActiveMenu('menu_slice');

		var boxcontent = '';
		var content = '<div class="col-md-12">';

		if (count == 0) {
			boxcontent += '<div data-help-label="" data-help-key="slicesList" />';
			boxcontent += '<p>No Slice found</p>';
		} else {
			boxcontent += '<div data-help-label="Name" data-help-key="name" />';
			boxcontent += '<div data-help-label="Actions" data-help-key="actions" />';
			boxcontent += '<table class="table table-hover table-striped"><thead><tr><th class="col-md-12">Name</th><th class="col-md-3">Actions</th></tr></thead><tboby>';
			$(slices).each(
					function() {
						
					boxcontent += '<tr><td>' + this.name + '</td>';
						
					boxcontent += '<td><div class="inline-toolbar" data-target-name="' + this.name
								+ '" data-target-object="slice"></div></td></tr>';
					});
			boxcontent += '</tbody><tfoot></tfoot></table>';
		}

		content += condensedBoxPrimary('Slice <span class="badge">'
				+ count + '</span>', boxcontent);
		content += '</div>';

		$(targetDom).html(content);

		inlineConfigViewButton('.inline-toolbar');
		inlineConfigUpdateButton('.inline-toolbar');
		inlineConfirmDeleteButton('.inline-toolbar', targetDom,
					$.Slice.actionRemove);
		
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

		$.Slice.help();
	},

	adjustNodeList : function(slice, readonly) {
		readonly = readonly || false;
		var content = '';
		if (!readonly) {
			if($(slice.nodes).length > 0) {
				$(slice.nodes).each(function(indexInArray, value) {
					content += removableNodeName(slice,value.name, 'data-input-l2');
				});
			} else {
				content += removableNodeName(slice, null, 'data-input-l2');
			}
			content += '<button type="button" class="btn btn-default btn-sm btn-block btn-add"><span class="glyphicon glyphicon-plus"></span> Add</button>';
		} else {
			$(slice.nodes).each(function(indexInArray, value) {
				content += removableNodeName(slice, value.name, 'data-input-l2', readonly);
			});
		}
		$('#edit-node-list').html(content);

		if (!readonly) {
			$('#edit-node-list').find('button.btn-add').click(function() {
				
				$(this).before('' + removableNodeName(slice, null, 'data-input-l2'));
				nodeListChange(slice);
				
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

$.Slice.WS = {

		update : function(inputData, successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			postValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/slice',
					inputData, successHandler, errorHandler, targetDom,
					'modal-body');
		},

		create : function(inputData, successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			putValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/slice',
					inputData, successHandler, errorHandler, targetDom,
					'modal-body');
		},

		list : function(successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			
			getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/slice',
					successHandler, errorHandler, targetDom);
		},

		remove : function(name, successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			deleteValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/slice/'
					+ name, successHandler, errorHandler, targetDom, 'modal-body');
		},

		read : function(name, successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/slice/' + name,
					successHandler, errorHandler, targetDom);
		},

		template : function(successHandler, errorHandler, targetDom) {
			errorHandler = errorHandler || null;
			getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/slice/template',
					successHandler, errorHandler, targetDom);
		},

		getCachedInventoryNodeList : function() {
			var cacheNode = Preloader.getResource('inventoryNode').inventoryNodes;
			return $(cacheNode);
		},
		
};

function nodeListChange(slice) {
	
	var totNode = $('#edit-node-list').find('div.row').length;
		
	if (totNode <= 1) {
	
		var btn = $('#edit-node-list').find('span.glyphicon-minus').parent('button.btn');
		btn[0].disabled = true;
	}
	
	$.Slice.help();
};

function removableNodeName(slice, nodeName, inputclass, readonly, hideLabels) {
	readonly = readonly || false;
	var nodeNamesList = [];
	$.Slice.WS.getCachedInventoryNodeList().each(function() {
			var sn = this.name + ' - ' + this.ip;

			nodeNamesList.push({
				'value' : this.name,
				'label' : sn
			});
	});
	nodeName = nodeName || "";

	var html = '';
	html += '<div class="row ' + inputclass + '">';
	html += '<div class="col-xs-11">';
	
	html += '<label>Name</label>';
	html += dynamicSelect('name', nodeName, nodeNamesList);
	html += '</div>';

	html += '<div class="col-xs-1">';
	html += '<label>&nbsp;</label>';
	
	$.serv = slice;
	html += '<div><button onclick="javascript: \
						if($(\'#edit-node-list\').find(\'span.glyphicon-minus\').length > 1)\
						{$(this).parent().parent().parent().remove();nodeListChange($.serv);}"\
			 class="btn btn-default btn-flat btn-sm pull-right" type="button"><span class="glyphicon glyphicon-minus"></span></button></div>';
	html += '</div>';
	html += '</div>';
	return html;
};

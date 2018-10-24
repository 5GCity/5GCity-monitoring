$.Node = {
	
	listPageLink : 'configuration.jsp?operation=list&object=inventoryNode',

	listPage : function(successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Node.displayListPage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Node.WS.list(successHandler, errorHandler, targetDom);
	},

	createPage : function(successHandler, errorHandler, targetDom) {
		successHandler = successHandler || $.Node.displayCreatePage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Node.WS.template(successHandler, errorHandler, targetDom);
	},

	readPage : function(name, successHandler, errorHandler, targetDom) {
		successHandler = successHandler
				|| $.Node.displayReadOnlyPage;
		errorHandler = errorHandler || null;
		targetDom = targetDom || $('#main-container');
		$.Node.WS
				.read(name, successHandler, errorHandler, targetDom);
	},

	actionCreate : function() {
		$.Node.WS.create(buildInput($('#main-container')),
				$.Node.goToListPage);
	},

	actionRemove : function(name) {
		$.Node.WS.remove(name, $.Node.goToListPage);
	},

	goToListPage : function() {
		window.location.href = $.Node.listPageLink;
	},

	displayReadOnlyPage : function(data, targetDom) {
		var obj = data.data;

		listAllButton('#nav-toolbar', obj.name, 'Node');
		confirmDeleteButton('#nav-toolbar', obj.name,
					$.Node.actionRemove);
		
		setPageTitle('Inventory Nodes: '+ obj.name);
		setActiveMenu('menu_Node');

		$.Node.displayFormData(obj, targetDom, true, true);
	},

	displayCreatePage : function(data, targetDom) {
		var obj = data.data;

		setPageTitle("Create Node");
		setActiveMenu('menu_node');

		saveButton('#nav-toolbar', $.Node.actionCreate);
		cancelButton('#nav-toolbar', $.Node.listPageLink);
		
		$.Node.displayFormData(obj, targetDom);
	},

	displayUpdatePage : function(data, targetDom) {
		var obj = data.data;

		saveButton('#nav-toolbar', $.Node.actionUpdate, obj.name);
		cancelButton('#nav-toolbar', $.Node.listPageLink);

		setPageTitle("Update Node: " + obj.name);
		setActiveMenu('menu_node');

		$.Node.displayFormData(obj, targetDom, true);
	},

	displayFormData : function(inventoryNode, targetDom, update, readonly) {
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
		boxgeneral += '<div class="form-group data-input-l1" ' + display
				+ '><label>Name</label>';
		boxgeneral += inputText('name', inventoryNode.name);
		boxgeneral += '</div>';
		boxgeneral += '<div class="form-group data-input-l1" ><label>Ip Address</label>';
		boxgeneral += inputText('ip', inventoryNode.ip);
		boxgeneral += '</div>';
		boxgeneral += '<div id="edit-port" class="form-group data-input-l1"><label>Port</label>';
		boxgeneral += inputNumber('port', inventoryNode.port);
		boxgeneral += '</div>';

		content += condensedBoxPrimary('Settings', boxgeneral);

		content += '</form>';

		$(targetDom).html(content);

		if (readonly) {
			setReadOnlyForm($(targetDom));
		}
		$.Node.help();
	},

	help : function() {

	},

	displayListPage : function(data, targetDom) {

		var inventoryNodes = data.data.inventoryNodes;
		
		var count = $(inventoryNodes).length;

		createButton('inventoryNode', '#nav-toolbar');
		
		setPageTitle('Inventory Nodes');
		setActiveMenu('menu_node');

		var boxcontent = '';
		var content = '<div class="col-md-12">';

		if (count == 0) {
			boxcontent += '<div data-help-label="" data-help-key="nodesList" />';
			boxcontent += '<p>No Node found</p>';
		} else {
			boxcontent += '<div data-help-label="Name" data-help-key="name" />';
			boxcontent += '<div data-help-label="Ip Address" data-help-key="ip" />';
			boxcontent += '<div data-help-label="Port" data-help-key="port" />';
			boxcontent += '<div data-help-label="Actions" data-help-key="actions" />';
			boxcontent += '<table class="table table-hover table-striped"><thead><tr><th class="col-md-3">Name</th><th class="col-md-10">Ip Address</th><th class="col-md-3">Port</th><th class="col-md-3">Actions</th></tr></thead><tboby>';
			$(inventoryNodes).each(
					function() {
						
					boxcontent += '<tr><td>' + this.name + '</td><td>' + this.ip + '</td><td>' + this.port + '</td>';
						
					boxcontent += '<td><div class="inline-toolbar" data-target-name="' + this.name
								+ '" data-target-object="inventoryNode"></div></td></tr>';
					});
			boxcontent += '</tbody><tfoot></tfoot></table>';
		}

		content += condensedBoxPrimary('Node <span class="badge">'
				+ count + '</span>', boxcontent);
		content += '</div>';

		$(targetDom).html(content);

		inlineConfigViewButton('.inline-toolbar');
		inlineConfirmDeleteButton('.inline-toolbar', targetDom,
					$.Node.actionRemove);

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

		$.Node.help();
	},

};

$.Node.WS = {

	create : function(inputData, successHandler, errorHandler, targetDom) {
		console.log("Sono qui WS!");
		errorHandler = errorHandler || null;
		putValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/inventoryNode',
				inputData, successHandler, errorHandler, targetDom,
				'modal-body');
	},

	list : function(successHandler, errorHandler, targetDom) {
		console.log("Sono in list!");
		errorHandler = errorHandler || null;
		
		getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/inventoryNode',
				successHandler, errorHandler, targetDom);
	},

	remove : function(name, successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		deleteValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/inventoryNode/'
				+ name, successHandler, errorHandler, targetDom, 'modal-body');
	},

	read : function(name, successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/inventoryNode/' + name,
				successHandler, errorHandler, targetDom);
	},

	template : function(successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/inventoryNode/template',
				successHandler, errorHandler, targetDom);
	},

};



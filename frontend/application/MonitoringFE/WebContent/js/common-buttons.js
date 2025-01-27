function inlineDeleteButton(inlineTargetDom, targetDom, actionCallback, confirmName) {
	confirmName = confirmName || false;
	$(inlineTargetDom)
	.each(
			function(idx) {
				var name = $(this).attr('data-target-name');//abbey generalizzazione migliore della funzione di delete confirm
				var deleteMsg = "Warning: are you sure you want to delete '<strong>"+confirmName ? '' : name+"</strong>'?";
				var did = 'confirm-delete-' + idx;
				if((name != "Monitoring") && (name != "ServiceMonitoring")) {
					var toolbarcontent = ' <button class="btn btn-sm btn-danger btn-flat btn-delete" data-toggle="modal" data-target="#'
						+ did + '" role="button"><span class="glyphicon glyphicon-trash"></span></button>';
					toolbarcontent += confirmDialog(did, deleteMsg);
					$(this).append(toolbarcontent);

					$('#' + did + ' .btn-confirm').click(function() {
						actionCallback(name, targetDom);
					});
				}
			});
}

function cancelButton(targetDom, cancelUrl) {
	var toolbarcontent = '<a class="btn btn-flat btn-default pull-right" href="' + cancelUrl
			+ '" role="button"><span class="glyphicon glyphicon-menu-left"></span> Cancel</a>';
	$(targetDom).append(toolbarcontent);
}

function backButton(targetDom, cancelUrl) {
	var toolbarcontent = '<a class="btn btn-flat btn-default pull-right" href="' + cancelUrl
			+ '" role="button"><span class="glyphicon glyphicon-menu-left"></span> Back</a>';
	$(targetDom).append(toolbarcontent);
}

function saveButton(targetDom, saveCallback, name) {
	var toolbarcontent = '<a class="btn btn-flat btn-primary btn-save pull-right" href="#" '
			+ 'role="button"><span class="glyphicon glyphicon-ok"></span> Save</a> ';
	$(targetDom).append(toolbarcontent);

	$(targetDom + ' .btn-save').click(function() {
		saveCallback(name);
	});
}

function drawCreateButton(objType, domTarget, page) {
	var content = '';
	content += '<a class="btn btn-primary btn-flat pull-right" ' + 'href="' + page + '?operation=create&object='
			+ objType + '" role="button"><span class="glyphicon glyphicon-plus"></span>'
			+ '<span class="hidden-sm hidden-xs">  New</span></a> ';
	if (domTarget) {
		$(domTarget).append(content);
	} else {
		return content;
	}
}

function createButton(objType, domTarget) {
	drawCreateButton(objType, domTarget, 'configuration.jsp');
}

function wizardProfileCreateButton(objType, domTarget) {
	drawCreateButton(objType, domTarget, 'wizard-profile.jsp');
}

function tracingCreateButton(objType, domTarget) {
	drawCreateButton(objType, domTarget, 'tracing.jsp');
}

function buttonAdd(objType, domTarget) {
	var content = '';
	content += '<a class="btn btn-primary btn-flat" ' + 'href="configuration.jsp?operation=create&object=' + objType
			+ '" role="button"><span class="glyphicon glyphicon-plus"></span> Add</a> ';
	if (domTarget) {
		$(domTarget).append(content);
	} else {
		return content;
	}
}

function confirmDeleteButton(targetDom, name, actionCallback) {
	var toolbarcontent = ' <button class="btn btn-danger btn-flat btn-delete pull-right" '
			+ 'data-toggle="modal" data-target="#confirm-delete-dialog" '
			+ 'role="button"><span class="glyphicon glyphicon-trash"></span><span class="hidden-sm hidden-xs"> Delete</span></button>';
	$(targetDom).append(toolbarcontent);

	$('#confirm-delete-dialog .btn-confirm').click(function() {
		actionCallback(name);
	});

}

function drawListAllButton(targetDom, name, objType, page) {
	var toolbarcontent = '<a class="btn btn-default btn-flat pull-right" href="' + page + '?operation=list&object='
			+ objType
			+ '&name='
			+ name
			+ '"><span class="glyphicon glyphicon-list"></span>'
			+ '<span class="hidden-sm hidden-xs"> List all</span></a> ';
	$(targetDom).append(toolbarcontent);
}

function listAllButton(targetDom, name, objType) {
	drawListAllButton(targetDom, name, objType, 'configuration.jsp');
}

function wizardProfileListAllButton(targetDom, name, objType) {
	drawListAllButton(targetDom, name, objType, 'wizard-profile.jsp');
}

function tracingListAllButton(targetDom, name, objType) {
	drawListAllButton(targetDom, name, objType, 'tracing.jsp');
}

function drawUpdateButton(targetDom, name, objType, page) {
	var toolbarcontent = '<a class="btn btn-primary btn-flat pull-right" '
			+ 'href="' + page + '?operation=edit&object=' + objType + '&name=' + name
			+ '" role="button"><span class="glyphicon glyphicon-pencil"></span>'
			+ '<span class="hidden-sm hidden-xs">  Edit</span></a> ';
	$(targetDom).append(toolbarcontent);
}

function configUpdateButton(targetDom, name, objType) {
	drawUpdateButton(targetDom, name, objType, 'configuration.jsp');
}

function wizardProfileUpdateButton(targetDom, name, objType) {
	drawUpdateButton(targetDom, name, objType, 'wizard-profile.jsp');
}

function tracingUpdateButton(targetDom, name, objType) {
	drawUpdateButton(targetDom, name, objType, 'tracing.jsp');
}

function drawInlineUpdateButton(inlineTargetDom, name, objType, page) {
	$(inlineTargetDom).each(
			function(idx) {
				var name = $(this).attr('data-target-name');
				var objType = $(this).attr('data-target-object');

				var toolbarcontent = '<a class="btn btn-sm btn-primary btn-flat" '
					+ 'href="' + page + '?operation=edit&object=' + objType + '&name=' + name
					+ '" role="button"><span class="glyphicon glyphicon-pencil"></span></a> ';
				$(this).append(toolbarcontent);

			});
}

function inlineConfigUpdateButton(inlineTargetDom, name, objType) {
	drawInlineUpdateButton(inlineTargetDom, name, objType, 'configuration.jsp');
}

function drawInlineUserUpdateButton(inlineTargetDom, name, objType, page) {
	$(inlineTargetDom).each(
			function(idx) {
				var name = $(this).attr('data-target-name');
				var objType = $(this).attr('data-target-object');
				var toUpdate = $(this).attr('data-target-update');
				if(toUpdate == "true") {
					var toolbarcontent = '<a class="btn btn-sm btn-primary btn-flat" '
						+ 'href="' + page + '?operation=edit&object=' + objType + '&name=' + name
						+ '" role="button"><span class="glyphicon glyphicon-pencil"></span></a> ';
					$(this).append(toolbarcontent);
				} else {
					var toolbarcontent = '<a class="btn btn-sm btn-primary btn-flat disabled" '
						+ 'href="' + page + '?operation=edit&object=' + objType + '&name=' + name
						+ '" role="button"><span class="glyphicon glyphicon-pencil"></span></a> ';
					$(this).append(toolbarcontent);
				}
			});
}

function inlineUserUpdateButton(inlineTargetDom, name, objType) {
	drawInlineUserUpdateButton(inlineTargetDom, name, objType, 'user-management.jsp');
}

function drawCreateButton(objType, domTarget, page) {
	var content = '';
	content += '<a class="btn btn-primary btn-flat pull-right" ' + 'href="' + page + '?operation=create&object='
			+ objType + '" role="button"><span class="glyphicon glyphicon-plus"></span>'
			+ '<span class="hidden-sm hidden-xs">  New</span></a> ';
	if (domTarget) {
		$(domTarget).append(content);
	} else {
		return content;
	}
}

function importButton(targetDom, inputFileId) {
	var content = '<span class="file-input btn btn-primary btn-file flat pull-right"><span class="glyphicon glyphicon-arrow-up"></span><span class="hidden-sm hidden-xs"> Import</span><input type="file" id="' + inputFileId + '"></span>';
	content += '<input type="text" class="form-control inline pull-right" placeholder="New name or suffix" id="newNameOrSuffix">';
	if (targetDom) {
		$(targetDom).append(content);
	} else {
		return content;
	}
}

function exportAllButton(targetDom, exportUrl) {
	var content = '<a class="btn btn-primary btn-flat pull-right" ' + 'href="' + exportUrl + '" role="button"><span class="glyphicon glyphicon-arrow-down"></span>'
			+ '<span class="hidden-sm hidden-xs"> Export All</span></a> ';
	if (targetDom) {
		$(targetDom).append(content);
	} else {
		return content;
	}
}

function inlineExportButton(inlineTargetDom, exportUrl) {
	$(inlineTargetDom).each(
			function(idx) {
				var name = $(this).attr('data-target-name');
				var toolbarcontent = '<a class="btn btn-sm btn-default btn-flat" href="'+exportUrl+'/'+name+'" role="button"><span class="glyphicon glyphicon-arrow-down"></span></a> ';
				$(this).append(toolbarcontent);
			});
}

function drawDashboardDetailButton(inlineTargetDom, dashbordUrl) {
	$(inlineTargetDom)
			.each(
					function(idx) {
						var name = $(this).attr('data-target-name');
						var objType = $(this).attr('data-target-object');
						var nameService = $(this).attr('data-service-name');
						if(nameService != "") {
						  var toolbarcontent = '<a class="btn btn-sm btn-default btn-flat" href="' + dashbordUrl + name + '&var-myservice=' + nameService + '" role="button"><span class="glyphicon glyphicon-dashboard"></span></a> ';
						  $(this).append(toolbarcontent);
						} else {
							  var toolbarcontent = '<a class="btn btn-sm btn-default btn-flat disabled"  href="' + dashbordUrl + name + '&var-myservice=' + nameService + '" role="button"><span class="glyphicon glyphicon-dashboard"></span></a> ';
							  $(this).append(toolbarcontent);
						}
					});
}

function drawDashboardGenericButton(inlineTargetDom) {
	$(inlineTargetDom)
	.each(
			function(idx) {
				var name = $(this).attr('data-target-name');
				var objType = $(this).attr('data-target-object');				
				var dashboardArray = $(this).data('dash-type').split(",");

				var dLen, i;
				dLen = dashboardArray.length;
				console.log("dashboardArray lenght : ",dashboardArray.length);
			
				var toolbarcontent = '<div class="form-group">';
				toolbarcontent += '<div class="input-group-btn">';
				toolbarcontent += '<button type="button" class="btn btn-primary btn-sm dropdown-toggle" '
						   + 'data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
						   +'<span class="currSel">Dashboard </span><span class="caret"></span></button>';
				toolbarcontent += '<ul class="dropdown-menu" role="menu">';
				
				for (i = 0; i < dLen; i++) {
					toolbarcontent += '<li><a href="#" id="' + name + '_' + dashboardArray[i] +'">'+ dashboardArray[i] +'</a></li>';
				}

				toolbarcontent += '</ul>';
				toolbarcontent += '</div>';
				$(this).append(toolbarcontent);				
				
			});
	
			$('#main-container').find('ul.dropdown-menu').find('a[href="#"]').click(function() {
				//console.log($(this).attr('id'));
		
				var myNameId = $(this).attr('id').split("_");

				if(myNameId[1] == "APACHE") {
					var dashUrl = '/dashboard/db/apache?refresh=1h&orgId=1';
					window.location.href = 'index.jsp?dashboardUrl=' + dashUrl + '';
				} else if(myNameId[1] == "NODE") {
					console.log("ho clickato NODE");
					var dashUrl = '/dashboard/db/node-view?refresh=1h&orgId=1&var-service=';
					window.location.href = 'index.jsp?dashboardUrl=' + dashUrl + myNameId[0] + '';
				}
			});

}

function drawDashboardSummaryButton(inlineTargetDom, dashbordUrl) {
	$(inlineTargetDom)
			.each(
					function(idx) {
						var name = $(this).attr('data-target-name');
						var objType = $(this).attr('data-target-object');
						var toolbarcontent = '<a class="btn btn-sm btn-default btn-flat" href="' + dashbordUrl + name +  '" role="button"><span class="glyphicon glyphicon-dashboard"></span></a> ';
						$(this).append(toolbarcontent);
					});
}

function drawInlineViewButton(inlineTargetDom, page) {
	$(inlineTargetDom)
			.each(
					function(idx) {
						var name = $(this).attr('data-target-name');
						var objType = $(this).attr('data-target-object');
						var toolbarcontent = '<a class="btn btn-sm btn-default btn-flat" href="' + page + '?operation=read&object='
								+ objType
								+ '&name='
								+ name
								+ '" role="button"><span class="glyphicon glyphicon-search"></span></a> ';
						$(this).append(toolbarcontent);
					});
}

function drawInlineListDetailsButton(inlineTargetDom) {
	$(inlineTargetDom)
		.each(
			function(idx) {
				var toolbarcontent = '<a class="btn btn-sm btn-default btn-flat listDetailsButton" role="button"><span class="ion ion-network"></span></a> ';
				$(this).append(toolbarcontent);
			}
		);
}


function inlineDashboardDetailButton(inlineTargetDom,dashUrl) {
	drawDashboardDetailButton(inlineTargetDom,'index.jsp?dashboardUrl='+dashUrl+'');
}
function inlineDashboardGenericButton(inlineTargetDom) {
  drawDashboardGenericButton(inlineTargetDom);
}
function inlineDashboardSummaryButton(inlineTargetDom,dashUrl) {
	drawDashboardSummaryButton(inlineTargetDom,'index.jsp?dashboardUrl='+dashUrl+'');
}
function inlineConfigViewButton(inlineTargetDom) {
	drawInlineViewButton(inlineTargetDom, 'configuration.jsp');
}
function inlineListDetailsButton(inlineTargetDom) {
	drawInlineListDetailsButton(inlineTargetDom);
}
function wizardProfileInlineViewButton(inlineTargetDom) {
	drawInlineViewButton(inlineTargetDom, 'wizard-profile.jsp');
}

function tracingInlineViewButton(inlineTargetDom) {
	drawInlineViewButton(inlineTargetDom, 'tracing.jsp');
}

function inlineConfirmDeleteButton(inlineTargetDom, targetDom, actionCallback) {
	inlineDeleteButton(inlineTargetDom, targetDom, actionCallback, true);
}

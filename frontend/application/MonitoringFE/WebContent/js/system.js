var _RELOAD_SERVICES = '/reload';
var _APP_SERVICES = '/app';
var _OM_CONF_SERVICES = '/om/conf';
var _MODULES_DATA_TABLE;

function WS_reloadConfiguration(handler, targetDom, forced, overlayDom) {
	forced = forced || false;
	postValues(_BASE_WEB_ROOT + _RELOAD_SERVICES + '?forced=' + forced, {}, handler, null, targetDom, overlayDom);
}
function WS_reloadICP(handler, targetDom, forced, overlayDom) {
	forced = forced || false;
	postValues(_BASE_WEB_ROOT + _RELOAD_SERVICES + '/reloadPlatformBus?forced=' + forced, {}, handler, null, targetDom,
			overlayDom);
}
function WS_reloadPlatDB(handler, targetDom, overlayDom) {
	postValues(_BASE_WEB_ROOT + _RELOAD_SERVICES + '/reloadPlatformDB', {}, handler, null, targetDom, overlayDom);
}
function WS_isPlatformDBConfSync(handler, targetDom, overlayDom) {
	getValues(_BASE_WEB_ROOT + _RELOAD_SERVICES + '/isPlatformDBConfSync', handler, null, targetDom, overlayDom);
}
function WS_reloadAlarms(handler, targetDom, overlayDom) {
	postValues(_BASE_WEB_ROOT + _RELOAD_SERVICES + '/reloadActiveAlarms', {}, handler, null, targetDom, overlayDom);
}
function WS_isConfSync(handler, targetDom, overlayDom) {
	getValues(_BASE_WEB_ROOT + _RELOAD_SERVICES + '/isConfSync', handler, null, targetDom, overlayDom);
}
function WS_isPlatformBusConfSync(handler, targetDom, overlayDom) {
	getValues(_BASE_WEB_ROOT + _RELOAD_SERVICES + '/isPlatformBusConfSync', handler, null, targetDom, overlayDom);
}
function WS_fullBackup(handler, targetDom, overlayDom) {
	getValues(_BASE_WEB_ROOT + _APP_SERVICES + '/fullBackup', handler, null, targetDom, overlayDom);
}
function WS_SBCFunctionalityStatus(handler, targetDom, overlayDom) {
	functionality = $('select#sbcfunctionality').val();
	getValues(_BASE_WEB_ROOT + _RELOAD_SERVICES + '/isSBCFunctionalityStatus/'+functionality, handler, null, targetDom, overlayDom);
}
function WS_restartSBCFunctionality(handler, errorHandler, targetDom, overlayDom) {
	functionality = $('select#sbcfunctionality').val();
	postValues(_BASE_WEB_ROOT + _RELOAD_SERVICES + '/restartSBCFunctionality/'+functionality, {}, handler, errorHandler, targetDom, overlayDom);
}

function showConfPage(data, targetDom) {
	var confProps = data.data.configItems;
	var content = '';
	var count = $(confProps).length;

	if (count == 0) {
		content += 'No configuration property';
		$(targetDom).html(content);
	} else {
		content += '<table class="table table-hover table-striped">';
		content += '<thead></thead>';
		content += '<tbody id="system-conf-table"></tbody>';
		content += '<tfoot></tfoot>';
		content += '</table>';
		$(targetDom).html(content);

		$(confProps).each(
				function(idx) {
					var prop_name = this.key;
					var isDefault = this.value === this.defaultValue;
					var ivid = 'input-value-' + idx;
					var rid = 'reset-' + idx;
					var eid = 'edit-' + idx;

					var row = '<tr>';
					row += '<td>';
					if (isDefault) {
						row += prop_name;
					} else {
						row += '<b>' + prop_name + '</b>';
					}

					var popover = '';
					popover += 'Description: ' + (this.description == null ? "-Not Defined-" : this.description);
					popover += '<br>Default value: <strong>'
							+ (this.defaultValue == null ? "-Not Defined-" : this.defaultValue) + '</strong>';

					row += '</td>';

					row += '<td><div class="input-group">';
					row += '<input id="' + ivid + '" type="text" class="form-control" value="'
							+ (this.value == null ? "" : this.value) + '">';
					row += '<span class="input-group-btn">';
					row += '<button class="btn btn-primary" type="button" id="' + eid
							+ '"><span class="glyphicon glyphicon-ok"></span></button>';
					row += '</span></div></td>';

					row += '<td><div class="inline-toolbar">';

					row += '<a tabindex="' + idx
							+ '" class="btn btn-primary" type="button" data-container="body" data-toggle="popover" '
							+ 'data-placement="left" data-html="true" data-trigger="focus" data-content="' + popover
							+ '">';
					row += '<span class="glyphicon glyphicon-info-sign"></span></a>';

					row += ' <button class="btn btn-danger" type="button" data-toggle="modal" data-target="#' + rid
							+ '" ' + (isDefault ? ' disabled="disabled"' : '')
							+ '><span class="glyphicon glyphicon-refresh"></span></button>';
					row += confirmDialog(rid, "Warning", "Are you sure you want to reset property '" + prop_name
							+ "' value to default" + (this.defaultValue == null ? "" : " '" + this.defaultValue + "'")
							+ "?");
					row += '</div></td></tr>';

					$('#system-conf-table').append(row);

					$('#' + eid).click(function() {
						$('.display-message').empty();
						WS_putConfProp(prop_name, $('#' + ivid).val(), function(data, targetDom) {
							WS_getConf(showConfPage, targetDom);
							$('.display-message').each(function() {
								$(this).html('<div class="alert alert-success">' + data.message + '</div>');
							});
						}, targetDom, $('#list-om-config-widget'));
					});

					$('#' + rid + ' .btn-confirm').click(function() {
						$('.display-message').empty();
						WS_deleteConfProp(prop_name, function(data, targetDom) {
							WS_getConf(showConfPage, targetDom);
							$('.display-message').each(function() {
								$(this).html('<div class="alert alert-success">' + data.message + '</div>');
							});
						}, targetDom, $('#list-om-config-widget'));
					});

				});

		$(targetDom).find('[data-toggle="popover"]').popover();
		$(targetDom).find('table').dataTable({
			"order" : [ [ 0, "asc" ] ],
			"columns" : [ null, {
				"orderable" : false
			}, {
				"orderable" : false
			} ]
		});
		$(targetDom).find('input[type="search"]').focus();

	}

}

function showSystemModules(data, targetDom) {
	var serviceUnits = data.data.serviceUnits;
	var boxcontent = '';

	boxcontent += '<div class="table-responsive">';
	boxcontent += '<table class="table table-hover table-striped"><thead><tr><th>Name</th><th>Id</th><th>Protocol</th><th>Channel</th><th>Status</th><th>Address</th><th>Port</th></tr></thead><tboby>';

	$(serviceUnits).each(
			function() {
				var suName = '';
				if(this.applicationGroup) {
					suName += this.applicationGroup + '.';
				}
				if(this.application) {
					suName += this.application + '.';
				}
				suName += this.name;
				$(this.serviceInstances).each(
						function() {
							var suId = this.id;
							var nchannels = $(this.channels).length;

							if (nchannels == 0) {
								boxcontent += '<tr><td><strong>' + suName + '</strong></td>';
								boxcontent += '<td>' + suId + '</td>';
								boxcontent += '<td>-</td><td>-</td><td>-</td><td>-</td><td>-</td>';
								boxcontent += '</tr>';
							} else {
								$(this.channels).each(
										function() {
											boxcontent += '<tr><td><strong>' + suName + '</strong></td>';
											boxcontent += '<td>' + suId + '</td>';
											boxcontent += '<td>' + this.communicationProtocol + '</td>';
											boxcontent += '<td>' + this.serviceType + '</td><td>' + (this.status === 'NA' ? '-' : this.status)
													+ '</td><td>' + this.ipAddress + '</td><td>' + this.port
													+ '</td></tr>';
										});
							}
						});

			});
	boxcontent += '</tbody><tfoot></tfoot></table>';
	boxcontent += '</div>';

	$(targetDom).html(boxcontent);

	_MODULES_DATA_TABLE = $(targetDom).find('table').DataTable({
		"columnDefs" : [ {
			"visible" : false,
			"targets" : 0
		} ],
		"order" : [ [ 0, 'asc' ] ],
		"displayLength" : 100,
		"drawCallback" : function(settings) {
			var api = this.api();
			var rows = api.rows({
				page : 'current'
			}).nodes();
			var last = null;

			api.column(0, {
				page : 'current'
			}).data().each(function(group, i) {
				if (last !== group) {
					$(rows).eq(i).before('<tr class="group"><td colspan="6">' + group + '</td></tr>');
					last = group;
				}
			});
		}
	});
	$(targetDom).find('table').on( 'click', 'tr.group', function () {
        var currentOrder = _MODULES_DATA_TABLE.order()[0];
        if ( currentOrder[0] === 0 && currentOrder[1] === 'asc' ) {
        	_MODULES_DATA_TABLE.order( [ 0, 'desc' ] ).draw();
        }
        else {
        	_MODULES_DATA_TABLE.order( [ 0, 'asc' ] ).draw();
        }
    } );	
	$(targetDom).find('input[type="search"]').focus();

}

function WS_getConf(handler, targetDom) {
	getValues(_BASE_WEB_ROOT + _OM_CONF_SERVICES, handler, null, targetDom, targetDom);
}

function WS_deleteConfProp(confKey, handler, targetDom, overlayDom) {
	deleteValues(_BASE_WEB_ROOT + _OM_CONF_SERVICES + '/' + confKey, handler, null, targetDom, overlayDom);
}

function WS_putConfProp(confKey, confValue, handler, targetDom, overlayDom) {
	putValues(_BASE_WEB_ROOT + _OM_CONF_SERVICES + '/property', {
		"key" : confKey,
		"value" : confValue
	}, handler, null, targetDom, overlayDom);
}

$.BackupRestore = {
	help : function() {
		$('div#DataTables_Table_0_wrapper select[name="DataTables_Table_0_length"]').addClass("data-help-skip");
		$('[name^="cron-"]').addClass("data-help-skip");
		$('[name="keepLocalCopy"]').addClass("data-help-skip");
		$.Help.write([{"name":"Backup", "localHelp":true}]);
	},
	showDBBackups : function(data, targetDom) {
		var dbBackups = data.data.backups;
		var content = '';
		var count = $(dbBackups).length;

		if (count == 0) {
			content += 'No DB backup';
		} else {
			// content += '<div class="table-responsive">';
			content += '<table class="table table-hover table-striped"><thead><tr>';
			content += '<th>Backup Name</th>';
			content += '<th>Date</th>';
			content += '<th>Storage</th>';
			content += '<th>Actions</th></tr></thead><tboby>';

			$(dbBackups).each(
					function() {
						content += '<tr><td>' + this.name + '</td>';
						content += '<td>' + this.date + '</td>';
						if (this.remoteLocationId != null) {
							content += '<td>' + this.remoteLocationId + '</td>';
						} else {
							content += '<td>LOCAL</td>';
						}
						content += '<td><div class="inline-toolbar" data-target-name="' + this.name
								+ '" data-target-storage="' + this.remoteLocationId + '"></div></td></tr>';
					});
			content += '</tbody><tfoot></tfoot></table>';
			// content += '</div>';
		}

		$(targetDom).html(content);

		if (!isUser() && !isMassProv()) {
			$.BackupRestore.inlineDownloadRestoreDeleteToolbar('.inline-toolbar', targetDom, true, true);
		}

		$(targetDom).find('table').dataTable({
			"order" : [ [ 1, "desc" ] ],
			"columns" : [ null, null, null, {
				"orderable" : false
			} ]
		});
		$.BackupRestore.help();
	},

	showDBScheduledBackups : function(data, targetDom) {
		var dbBackups = data.data.scheduledBackups;
		var content = '';
		var count = $(dbBackups).length;

		if (count == 0) {
			content += 'No Scheduled DB backups';
		} else {
			content += '<div class="table-responsive">';
			content += '<table class="table table-hover table-striped"><thead><tr>';
			content += '<th>Backup Name</th>';
			content += '<th>Type</th>';
			content += '<th>Storage</th>';
			content += '<th>Actions</th></tr></thead><tboby>';

			$(dbBackups).each(
					function() {
						var bck = {}, bckType = '';
						if (this.oneshotBackup != null) {
							bck = this.oneshotBackup;
							bckType = 'Scheduled on ' + bck.executionTime;
						}
						if (this.cyclicBackup != null) {
							bck = this.cyclicBackup;
							bckType = 'Recurring ' + $.BackupRestore.displayQuartzString(bck.cronExpr);
						}
						content += '<tr><td>' + bck.name + '</td>';
						content += '<td>' + bckType + '</td>';
						if (bck.remoteLocationId != null) {
							content += '<td>' + bck.remoteLocationId + '</td>';
						} else {
							content += '<td>LOCAL</td>';
						}
						content += '<td><div class="scheduled-inline-toolbar" data-target-name="' + bck.name
								+ '" data-target-storage="' + bck.remoteLocationId + '"></div></td></tr>';

					});
			content += '</tbody><tfoot></tfoot></table>';
			content += '</div>';
		}

		$(targetDom).html(content);

		if (!isUser() && !isMassProv()) {
			$.BackupRestore.inlineDeleteScheduledToolbar('.scheduled-inline-toolbar', targetDom, true);
		}
	},

	prepareDBBackupOptions : function() {
		var delayedStartDate = moment().add(1, 'hours').format('YYYY-MM-DD HH:mm:ss');

		$('#delayed-start-time').daterangepicker({
			singleDatePicker : true,
			showDropdowns : true,
			timePicker : true,
			timePickerIncrement : 1,
			format : 'YYYY-MM-DD HH:mm:ss',
			minDate : moment().format('YYYY-MM-DD HH:mm:ss'),
			startDate : delayedStartDate,
			timePicker12Hour : false
		}, function(start) {
			$('#executionTime').val(start.format('YYYY-MM-DD HH:mm:ss'));
		});
		$('#delayed-start-time').val(delayedStartDate);
		$('#executionTime').val(delayedStartDate);

		$.SBCStorage.WS.list(function(data) {
			var storages = [ {
				value : '',
				label : 'LOCAL',
				type : 'DEFAULT'
			} ];
			$(data.data.storages).each(function() {
				storages.push({
					value : this.name,
					label : this.name,
					type : ''
				});
			});
			var scontent = '';
			scontent += '<div class="form-group data-input-l1"><label>Storage</label>';
			scontent += dynamicSelect('remoteLocationId', null, storages);
			scontent += '</div>';
			$('#storage-id').html(scontent);
			$.BackupRestore.toggleKeepLocal();
		});

		$('#cron-time').cron({
			initial : "0 3 * * *",
			onChange : function() {
				$('#cronExpr').val($(this).cron("value"));
			}
		});

		this.toggleScheduleTypeSelector();

		$('#startDBBackup').click(
				function() {
					var bckType = $('#schedule-type-selector').find('option:selected').val();
					if (bckType == 1) {
						$('.display-message').empty();
						$('#dbBackupName').attr('name', 'name');
						$('#delayed-options').addClass('data-input-skip');
						$('#schedule-options').addClass('data-input-skip');
						$.BackupRestore.WS.backupNow(buildInput($('#start-new-db-backup')), function(data, targetDom) {
							$.BackupRestore.WS.getBackups($.BackupRestore.showDBBackups, $('#dbBackupList'));
							$.BackupRestore.WS.getScheduledBackups($.BackupRestore.showDBScheduledBackups,
									$('#crondbBackupList'));
						}, null, $('#new-db-backup-widget'));
					}
					if (bckType == 2) {
						$('.display-message').empty();
						$('#dbBackupName').attr('name', 'name');
						$('#delayed-options').removeClass('data-input-skip');
						$('#schedule-options').addClass('data-input-skip');
						$.BackupRestore.WS.backupDelayed(buildInput($('#start-new-db-backup')), function(data,
								targetDom) {
							$.BackupRestore.WS.getBackups($.BackupRestore.showDBBackups, $('#dbBackupList'));
							$.BackupRestore.WS.getScheduledBackups($.BackupRestore.showDBScheduledBackups,
									$('#crondbBackupList'));
						}, null, $('#new-db-backup-widget'));
					}
					if (bckType == 3) {
						$('.display-message').empty();
						$('#dbBackupName').attr('name', 'name');
						$('#delayed-options').addClass('data-input-skip');
						$('#schedule-options').removeClass('data-input-skip');
						$.BackupRestore.WS.backupScheduled(buildInput($('#start-new-db-backup')), function(data,
								targetDom) {
							$.BackupRestore.WS.getBackups($.BackupRestore.showDBBackups, $('#dbBackupList'));
							$.BackupRestore.WS.getScheduledBackups($.BackupRestore.showDBScheduledBackups,
									$('#crondbBackupList'));
						}, null, $('#new-db-backup-widget'));
					}
				});
	},

	toggleScheduleTypeSelector : function() {
		$('#schedule-type-selector').change(function() {
			var selected = $(this).find('option:selected').val();
			if (selected == 1) {
				$('#delayed-options').hide();
				$('#schedule-options').hide();
			}
			if (selected == 2) {
				$('#delayed-options').show();
				$('#schedule-options').hide();
			}
			if (selected == 3) {
				$('#delayed-options').hide();
				$('#schedule-options').show();
			}
		});
		$('#schedule-type-selector').change();
	},

	toggleKeepLocal : function() {
		$('#klc').hide();
		$('#storage-id select').change(function() {
			var selected = $(this).find('option:selected').val();
			if (isNull(selected)) {
				$('#klc').hide();
				$('#klc select').val('false');
			} else {
				$('#klc').show();
			}
		});
	},

	showConfBackups : function(data, targetDom) {
		var confBackups = data.data.backups;
		var content = '';
		var count = $(confBackups).length;

		if (count == 0) {
			content += 'No configuration backup';
			$('#clear-all-conf-backup-widget button').attr("disabled", "disabled");
		} else {
			$('#clear-all-conf-backup-widget button').removeAttr("disabled");
			content += '<div class="table-responsive">';
			content += '<table class="table table-hover table-striped"><thead><tr>';
			content += '<th>Backup Name</th>';
			content += '<th>Date</th>';
			content += '<th>Storage</th>';
			content += '<th>Actions</th></tr></thead><tboby>';

			$(confBackups).each(
					function() {
						content += '<tr><td>' + this.name + '</td>';
						content += '<td>' + this.date + '</td>';
						if (this.remoteLocationId != null) {
							content += '<td>' + this.remoteLocationId + '</td>';
						} else {
							content += '<td>LOCAL</td>';
						}
						content += '<td><div class="inline-toolbar-conf" data-target-name="' + this.name
								+ '" data-target-storage="' + this.remoteLocationId + '"></div></td></tr>';
					});
			content += '</tbody><tfoot></tfoot></table>';
			content += '</div>';
		}

		$(targetDom).html(content);

		if (isSystemAdmin()) {
			$.BackupRestore.inlineDownloadRestoreDeleteToolbar('.inline-toolbar-conf', targetDom, false);
		}
	},

	inlineDeleteScheduledToolbar : function(inlineTargetDom, targetDom) {
		$(inlineTargetDom)
				.each(
						function(idx) {
							var deleteMsg = "Warning: are you sure you want to delete this scheduled backup?";
							var name = $(this).data('targetName');
							var storage = $(this).data('targetStorage');
							var dsid = 'confirm-delete-scheduled' + idx;
							var toolbarcontent = ' <button class="btn btn-sm btn-danger btn-flat btn-delete" data-toggle="modal" data-target="#'
									+ dsid + '" role="button"><span class="glyphicon glyphicon-trash"></span></button>';
							toolbarcontent += confirmDialog(dsid, deleteMsg);
							$(this).html(toolbarcontent);

							$('#' + dsid + ' .btn-confirm').click(
									function() {
										$('.display-message').empty();
										$.BackupRestore.WS.deleteScheduledBackup(name, storage, function(data,
												targetDom) {
											$('.display-message').each(
													function() {
														$(this).html(
																'<div class="alert alert-success">' + data.message
																		+ '</div>');
													});
											$.BackupRestore.WS.getScheduledBackups(
													$.BackupRestore.showDBScheduledBackups, $('#crondbBackupList'));
										}, targetDom, $('#crondbBackupList'));
									});
						});
	},

	inlineDownloadRestoreDeleteToolbar : function(inlineTargetDom, targetDom, isDBBackup, download) {
		download = download || false;
		$(inlineTargetDom)
				.each(
						function(idx) {
							var restoreMsg;
							var deleteMsg;
							if (isDBBackup) {
								restoreMsg = "Warning: are you sure you want to restore DB?";
								deleteMsg = "Warning: are you sure you want to delete DB backup?";
							} else {
								restoreMsg = "Warning: are you sure you want to restore configuration?";
								deleteMsg = "Warning: are you sure you want to delete configuration backup?";
							}

							var name = $(this).data('targetName');
							var storage = $(this).data('targetStorage');
							var rid;
							var did;
							var wid;
							if (isDBBackup) {
								rid = 'confirm-restore-' + idx;
								did = 'confirm-delete-' + idx;
								wid = 'btn-download-' + idx;
							} else {
								rid = 'confirm-restore-conf-' + idx;
								did = 'confirm-delete-conf-' + idx;
								wid = 'btn-download-conf-' + idx;
							}

							var toolbarcontent = '';
							if (download) {
								toolbarcontent += '<button class="btn btn-sm btn-info btn-flat "id="' + wid
										+ '" role="button" data-title="tooltip" title="Download">'
										+ '<span class="glyphicon glyphicon-download-alt"></span></button> ';
							}
							toolbarcontent += ' <button class="btn btn-sm btn-primary btn-flat" data-title="tooltip" data-toggle="modal" data-target="#'
									+ rid
									+ '" role="button" title="Restore"><span class="glyphicon glyphicon-hdd" ></span></button> ';
							toolbarcontent += confirmDialog(rid, restoreMsg);
							toolbarcontent += ' <button class="btn btn-sm btn-danger btn-flat btn-delete" data-title="tooltip" data-toggle="modal" data-target="#'
									+ did
									+ '" role="button" title="Delete"><span class="glyphicon glyphicon-trash"></span></button> ';
							toolbarcontent += confirmDialog(did, deleteMsg);
							$(this).html(toolbarcontent);

							$("[data-title='tooltip']").tooltip();

							$('#' + wid).click(function() {
								$.BackupRestore.WS.downloadBackup(name, storage);
							});
							$('#' + rid + ' .btn-confirm').click(
									function() {
										$('.display-message').empty();
										if (isDBBackup) {
											$.BackupRestore.WS.restoreFromBackup(name, storage, function(data,
													targetDom) {
												$('.display-message').each(
														function() {
															$(this).html(
																	'<div class="alert alert-success">' + data.message
																			+ '</div>');
														});
											}, targetDom, $('#list-db-backup-widget'));
										} else {
											$.BackupRestore.WS.restoreFromConfBackup(name, function(data, targetDom) {
												$('.display-message').each(
														function() {
															$(this).html(
																	'<div class="alert alert-success">' + data.message
																			+ '</div>');
														});
											}, targetDom, $('#list-conf-backup-widget'));
										}
									});

							$('#' + did + ' .btn-confirm').click(
									function() {
										$('.display-message').empty();
										if (isDBBackup) {
											$.BackupRestore.WS.deleteBackup(name, storage, function(data, targetDom) {
												$('.display-message').each(
														function() {
															$(this).html(
																	'<div class="alert alert-success">' + data.message
																			+ '</div>');
														});
												$.BackupRestore.WS.getBackups($.BackupRestore.showDBBackups,
														$('#dbBackupList'));
											}, targetDom, $('#list-db-backup-widget'));
										} else {
											$.BackupRestore.WS.deleteConfBackup(name, function(data, targetDom) {
												$('.display-message').each(
														function() {
															$(this).html(
																	'<div class="alert alert-success">' + data.message
																			+ '</div>');
														});
												$.BackupRestore.WS.getConfBackups($.BackupRestore.showConfBackups,
														targetDom);
											}, targetDom, $('#list-conf-backup-widget'));
										}
									});
						});
	},

	displayQuartzString : function(cron_str) {

		var $cronDom = $('<div></div>');

		$($.BackupRestore.cronExpressions).each(function() {
			if (this.expr.test(cron_str)) {
				var d = cron_str.split(" ");
				var v = {
					"mins" : d[1],
					"hour" : d[2],
					"dom" : d[3],
					"month" : d[4],
					"dow" : d[5]
				};
				$cronDom = $('<div>' + this.string + '</div>');
				$(this.subs).each(function() {
					var sub = this;
					var txt = v[sub];
					if (sub == 'mins' || sub == 'hour') {
						txt = $.BackupRestore.utils.zeroPad(txt, 2);
					}
					if (sub == 'dow') {
						txt = $.BackupRestore.utils.days[txt];
					}
					if (sub == 'dom') {
						txt = $.BackupRestore.utils.domTh(txt);
					}
					if (sub == 'month') {
						txt = $.BackupRestore.utils.months[txt];
					}
					$cronDom.find('span.' + sub).text(txt);
				});
				return false;
			}
		});
		return $cronDom.html();

	},

	adjustInput : function(input) {
		if (input.name === '') {
			input.name = "Backup_" + moment().format("YYYYMMDD_HHmmssSSS");
		}
		if (input.backupName === '') {
			input.backupName = "Backup_" + moment().format("YYYYMMDD_HHmmssSSS");
		}
		if (input.remoteLocationId === '') {
			delete input.remoteLocationId;
		}
		if (input.cronExpr != null) {
			input.cronExpr = '0 ' + input.cronExpr + ' *';
		}
		return input;
	}

};

$.BackupRestore.utils = {
	days : [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ],
	months : [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
			"November", "December" ],
	zeroPad : function(num, size) {
		var s = num + "";
		while (s.length < size) {
			s = "0" + s;
		}
		return s;
	},

	domTh : function(dom) {
		if (dom == 1) {
			return dom + 'st';
		}
		if (dom == 2) {
			return dom + 'nd';
		}
		if (dom == 3) {
			return dom + 'rd';
		}
		return dom + 'th';
	}

};

$.BackupRestore.cronExpressions = [
		{
			// "0 * * * * ? *"
			'expr' : /^0\s([\*?]\s){5}\[\*?]$/,
			'string' : 'Every minute',
			'subs' : []
		},
		{
			// "0 n * * * ? *"
			'expr' : /^0\s\d{1,2}\s([\*\?]\s){4}\[\*\?]$/,
			'string' : 'Every hour at <span class="mins"></span> minutes past the hour',
			'subs' : [ 'mins' ]
		},
		{
			// "0 n n * * ? *"
			'expr' : /^0\s(\d{1,2}\s){2}([\*\?]\s){3}[\*\?]$/,
			'string' : 'Every day at <span class="hour"></span>:<span class="mins"></span>',
			'subs' : [ 'mins', 'hour' ]
		},
		{
			// "0 n n ? * n *"
			'expr' : /^0\s(\d{1,2}\s){2}([\*\?]\s){2}\d{1,2}\s[\*\?]$/,
			'string' : 'Every week on <span class="dow"></span> at <span class="hour"></span>:<span class="mins"></span>',
			'subs' : [ 'dow', 'hour', 'mins' ]
		},
		{
			// "0 n n n * ? *"
			'expr' : /^0\s(\d{1,2}\s){3}([\*\?]\s){2}[\*\?]$/,
			'string' : 'Every month on the <span class="dom"></span> at <span class="hour"></span>:<span class="mins"></span>',
			'subs' : [ 'dom', 'hour', 'mins' ]
		},
		{
			'expr' : /^0\s(\d{1,2}\s){4}[\*\?]\s[\*\?]$/,
			'string' : 'Every year on the <span class="dom"></span> of <span class="month"></span> at <span class="hour"></span>:<span class="mins"></span>',
			'subs' : [ 'dom', 'month', 'hour', 'mins' ]
		} ];

$.BackupRestore.WS = {

	_DB_SERVICES : '/db',
	_OM_CONF_SERVICES : '/om/conf',

	/* DB LIST */

	getBackups : function(handler, targetDom) {
		getValues(_BASE_WEB_ROOT + this._DB_SERVICES + '/backup', handler, null, targetDom, targetDom);
	},

	getScheduledBackups : function(handler, targetDom) {
		getValues(_BASE_WEB_ROOT + this._DB_SERVICES + '/scheduledBackup', handler, null, targetDom, targetDom);
	},

	/* DOWNLOAD */

	downloadBackup : function(name, storage, handler, targetDom) {
		var qs = '';
		if (storage != null) {
			qs = '?remoteLocationId=' + storage;
		}
		// Workaround bug IE https://support.microsoft.com/en-us/kb/812935
		if ($.browser.msie && $.browser.version == "8.0") {
			window.location = 'http://' + window.location.host + _BASE_WEB_ROOT + this._DB_SERVICES
					+ '/backup/download/' + name + qs;
		} else {
			window.location = _BASE_WEB_ROOT + this._DB_SERVICES + '/backup/download/' + name + qs;
		}
	},

	/* DB CREATE OR SCHEDULE */

	backupNow : function(input, handler, targetDom, overlayDom) {
		postValues(_BASE_WEB_ROOT + this._DB_SERVICES + '/backup', $.BackupRestore.adjustInput(input), handler, null,
				targetDom, overlayDom);
	},

	backupDelayed : function(input, handler, targetDom, overlayDom) {
		input.backupName = input.name;
		postValues(_BASE_WEB_ROOT + this._DB_SERVICES + '/scheduledBackup/oneshotBackup', $.BackupRestore
				.adjustInput(input), handler, null, targetDom, overlayDom);
	},

	backupScheduled : function(input, handler, targetDom, overlayDom) {
		input.backupName = input.name;
		postValues(_BASE_WEB_ROOT + this._DB_SERVICES + '/scheduledBackup/cyclicBackup', $.BackupRestore
				.adjustInput(input), handler, null, targetDom, overlayDom);
	},

	/* RESTORE */

	restoreFromBackup : function(name, storage, handler, targetDom, overlayDom) {
		postValues(_BASE_WEB_ROOT + this._DB_SERVICES + '/restore', $.BackupRestore.adjustInput({
			name : name,
			remoteLocationId : storage
		}), handler, null, targetDom, overlayDom);
	},

	/* DELETE */

	deleteBackup : function(name, storage, handler, targetDom, overlayDom) {
		deleteValues(_BASE_WEB_ROOT + this._DB_SERVICES + '/backup', handler, null, targetDom, overlayDom,
				$.BackupRestore.adjustInput({
					name : name,
					remoteLocationId : storage
				}));
	},

	deleteScheduledBackup : function(name, storage, handler, targetDom, overlayDom) {
		deleteValues(_BASE_WEB_ROOT + this._DB_SERVICES + '/scheduledBackup/' + name, handler, null, targetDom);
	},

	deleteAllBackup : function(handler, targetDom, overlayDom) {
		deleteValues(_BASE_WEB_ROOT + this._DB_SERVICES + '/backup', handler, null, targetDom, overlayDom);
	},

	/* CONF */

	getConfBackups : function(handler, targetDom) {
		getValues(_BASE_WEB_ROOT + this._OM_CONF_SERVICES + '/backup', handler, null, targetDom, targetDom);
	},

	createConfBackup : function(confBackupName, handler, targetDom, overlayDom) {
		confBackupName = confBackupName || "Backup_" + getCurrentDateTime();
		postValues(_BASE_WEB_ROOT + this._OM_CONF_SERVICES + '/backup/' + confBackupName, {}, handler, null, targetDom,
				overlayDom);
	},

	restoreFromConfBackup : function(confBackupName, handler, targetDom, overlayDom) {
		postValues(_BASE_WEB_ROOT + this._OM_CONF_SERVICES + '/restore/' + confBackupName, {}, handler, null,
				targetDom, overlayDom);
	},

	deleteConfBackup : function(confBackupName, handler, targetDom, overlayDom) {
		deleteValues(_BASE_WEB_ROOT + this._OM_CONF_SERVICES + '/backup/' + confBackupName, handler, null, targetDom,
				overlayDom);
	},

	deleteAllConfBackup : function(handler, targetDom, overlayDom) {
		deleteValues(_BASE_WEB_ROOT + this._OM_CONF_SERVICES + '/backup', handler, null, targetDom, overlayDom);
	}
};

$.LogCollector = {
		status : 'IDLE',
		categories : {
    		'signaling' : false,
    		'media' : false,
    		'ha' : false,
    		'system' : false
		},
		logLink :  _BASE_WEB_ROOT + '/log/collector',
		
		createPage : function() {
			var targetDom =  $('#main-container');
			var href = $.LogCollector.logLink + '/collectedLogs';
			
			function createBtnOnOff(id){
				
				var btnOnOff = '<input id="' + id +'_on_off" class="onOff btn btn-sm" type="checkbox" \
					data-toggle="toggle" data-off-text="off" data-on-text="on" data-size="small">';
				return btnOnOff;
			}
			var sp = '&nbsp';
			var btnStart = '<button class="collector-start btn btn-sm   btn-flat" style="width:75px;">'
				+ '<span><i class="fa fa-circle " style="color : #dd4b39"></i></span> <text> Start </text></button>';
			
			var btnStop = '<button class="collector-stop btn-sm btn btn-flat" '
				+' ><span ><i class="fa fa-stop  " style="color:#333;"></i></span> Stop</button>';
			var btnDown = '<a class="collector-download btn btn-default btn-flat btn-sm" href="'
				+  href
				+'" role="button">'
						+'<span class="glyphicon glyphicon-arrow-down"></span></a>';
			
			var btnCestino = '<button class="collector-trash btn btn-flat btn-sm btn-delete" ><span> <i class="glyphicon glyphicon-trash"></i></span></button>';
			
//			var iconLoading = '<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>';
			
			var boxStatus = inputText_X('status', 'IDLE', 'Status', 'readOnly="readOnly" id="statusInput"');
			
			var content = '';
			content += '<div class="col-md-8">';
			
			boxTable = '';
			
			boxTable += '<table class="table table-hover table-striped">'
				+'<thead>'
				+'<tr ><th class="row" colspan="2">&nbsp <div style="margin-bottom:0px;" class="form-group row col-md-4">'
				+ boxStatus +'</div></th></tr>'
				+'<tr><th class="row col-md-6">Category</th> <th class="row col-md-6">Action</th></tr>'
				+'</thead>';
			
			boxTable += '<tboby>';

			boxTable += '<tr><td>Signaling</td><td>' + createBtnOnOff('signaling') +' </td></tr>';
			boxTable += '<tr><td>Media</td><td>' + createBtnOnOff('media') +' </td></tr>';
			boxTable += '<tr><td>HA/IP</td><td>' + createBtnOnOff('ha') +' </td></tr>';
			boxTable += '<tr><td>System</td><td>' + createBtnOnOff('system') +' </td></tr>';
			
			boxTable += '</tbody>';
			boxTable += '<tfoot>';
			
			boxTable += '<tr><td><div class="row  col-md-12">'
				+btnStart + sp + btnStop + '</div></td><td><div class="row col-md-6">'
				+ btnDown + sp + sp + btnCestino +'</div></td></tr>';
			boxTable += '</tfoot></table>';
			
			content += condensedBoxPrimary('Log Collector', boxTable);
			
			content += '</div>'; // colmd8
			
			$(targetDom).append(content);
			$.LogCollector.WS('status');
			
			$('.onOff').bootstrapSwitch('state', false);
			
			$('.bootstrap-switch.bootstrap-switch-wrapper').css({
				'pointer-events' : 'none'
			});
			
			$('.bootstrap-switch-handle-off, .bootstrap-switch-handle-on').css({
				'pointer-events' : 'all'
			});
			
			$('.onOff').on('switchChange.bootstrapSwitch', function (e, btnIsOn) {
				$.LogCollector.changeCategoryStatus(this, btnIsOn);
				if(btnIsOn == true  && $('.bootstrap-switch-on').length ){
					unbindWarningInput($('#statusInput'));
					if($.LogCollector.status == 'IDLE' || $.LogCollector.status == 'PAUSE'){
						$('.collector-start').removeAttr('disabled');
					}
				}	
				else if(btnIsOn == false && $('.bootstrap-switch-on').length == 0){
					
					var bind = false;
					var msg = '';
//					if($.LogCollector.status == 'IDLE'){
//						bind = true;
//						msg = 'Activate at least one category to be able to start logging';
//						
//					}else
					if($.LogCollector.status == 'PAUSE'){
						bind = true;
						msg = 'Activate at least one category to resume logging or press Stop button to finish';
					}
					
					if(bind){
						$('.collector-start').attr('disabled',true);
						bindWarningInput($('#statusInput'),
								msg);
						$('#statusInput').parent().find('.glyphicon-warning-sign').css({
		        			 right : '15px',
		        		});
					}	
				}
			});
			
			$('.collector-start').click(function(){
				var icon = $('.collector-start').find('i');
				
				if($(icon).hasClass('fa-circle')){
					var warning = $.LogCollector.warningOnCategoriesDisabled();
					if(warning)
						return;
    					$.LogCollector.showWarningModal('<span class="glyphicon glyphicon-warning-sign"></span>&nbspWarning',
    							'it is not advisable to launch the logging operation when there are other running operations. Do you want to proceed?',
    							function(){
                					$.LogCollector.WS('start');
            					});
					
				}else if($(icon).hasClass('fa-play')){
					$.LogCollector.WS('resume');
				}else if($(icon).hasClass('fa-pause')){
					
					$.LogCollector.WS('pause');
				}
			});
			
			$('.collector-stop').click(function(){
				unbindWarningInput($('#statusInput'));
				$.LogCollector.WS('stop');
			});
			
			$('.collector-download').click(function(){
					$.LogCollector.setStatusToIdle();
			});
			
			$(' .collector-trash').click(function(){
				var callBack = function(){
					$.LogCollector.WS('delete');
				};
				$.LogCollector.showWarningModal(
						'<span class="glyphicon glyphicon-warning-sign"></span>&nbspWarning!',
						'Do you want to discart these logs?', callBack);
			});
		},
	changeCategoryStatus : function(toggleBtn, status) {
		var id = toggleBtn.id.replace('_on_off', '');
		$.LogCollector.categories[id] = status;
		},
		
		setStatusToIdle : function(){
			var st = 'IDLE';
			$.LogCollector.status = st;
			$('input[name="status"]').val(st);
			$('.collector-stop').prop('disabled',true);
			$('.collector-start').removeAttr('disabled');
		},
		
		setStatusToStarted : function(){
			
			var st = 'START';
			$.LogCollector.status = st;
			$('input[name="status"]').val('STARTED');
			$('.collector-start').attr('disabled',true);
			$('.collector-stop').removeAttr('disabled');
		},
		setStatusToStopped : function(){
			
			$('.collector-start').find('i')
								 .removeClass('fa-play')
								 .removeClass('fa-pause')
								 .addClass('fa-circle')
								 .css({color : '#dd4b39'});
			$('.collector-start').find('text').html('Start');
			
			var st = 'STOP';
			$.LogCollector.status = st;
			$('input[name="status"]').val('STOPPED');
			$('.collector-start').prop('disabled',true);
			$('.collector-stop').prop('disabled',true);
			
			$('.onOff').bootstrapSwitch('disabled', false);
			
		},
		
		setStatusToResume : function(){
			var icon = $('.collector-start').find('i');
			var txt = $('.collector-start').find('text');
			var st = 'PAUSE';
			$.LogCollector.status = st;
			$('input[name="status"]').val('PAUSED');
			$('.onOff').bootstrapSwitch('disabled', false);
			$(txt).html('Resume');
			$(icon).removeClass('fa-pause').addClass('fa-play');
			$(icon).css({'color':'#00a65a'});
		},
		setStatusToPause : function(){
			var icon = $('.collector-start').find('i');
			var txt = $('.collector-start').find('text');
			var st = 'START';
			$('.collector-stop').removeAttr('disabled');
			$.LogCollector.status = st;
			$('input[name="status"]').val('STARTED');
			$(txt).html('Pause');
			
			$(icon).removeClass('fa-play').removeClass('fa-circle').addClass('fa-pause');
			$(icon).css({'color':'#333'});
			$('.onOff').bootstrapSwitch('disabled', true);
		},
		
		warningOnCategoriesDisabled : function(){
			var theAreDisabled = false;
			if($('.bootstrap-switch-on').length){
				$('.collector-start').removeAttr('disabled');
				unbindWarningInput($('#statusInput'));
				theAreDisabled = false;
			}
			else{
				$('.collector-start').attr('disabled',true);
				bindWarningInput($('#statusInput'),
				'Activate at least one category to be able to start logging');
        		$('#statusInput').parent().find('.glyphicon-warning-sign').css({
        			 right : '15px'
        		});
        		theAreDisabled = true;
			}
			return theAreDisabled;
		},
		
	showWarningModal : function(title, modalBody, onConfirm, paramsForConfirm) {

		var confirmBtn = isNull(onConfirm) ? ''
				: '<button type="button" class="btn btn-primary btn-confirm" data-dismiss="modal">Confirm</button>';
		var cancelText = isNull(onConfirm) ? 'Ok' : 'Cancel';
		var modal = '<div id="warning-modal" class="modal" data-backdrop="static"' + 'data-keyboard="false" >'
				+ '<div class="modal-dialog">' + '<div class="modal-content">' + '<div class="modal-header">' + '<h4>'
				+ title + '</h4>'
				// + '<div clas="col-md-12">&nbsp' + modalBody + '</div>'
				+ '</div>' // modal header
				+ '<div class="row modal-body" style="max-height : calc(100vh - 10px); overflow-y:auto;">'
				+ '<center style="padding: 0px 15px 0px 15px">'
				+ modalBody + '</center>' + '</div>' // modal body
				+ '<div class="modal-footer">' + '<button type="button" class="btn btn-cancel" data-dismiss="modal">'
				+ cancelText + ' </button>' + confirmBtn + '</div>'// modal-footer
				+ '</div>' // modal-content
				+ '</div>' // modal-dialog
				+ '</div>'; // id

		$('#main-container').append(modal);
		$('#warning-modal').modal('show');
		$('.btn-cancel, .btn-confirm').css({
			'cursor' : 'pointer'
		});

		$('.btn-cancel').click(function() {
			$('#warning-modal').modal('hide');
			$('#warning-modal').remove();
		});

		$('.btn-confirm').click(function() {
			if (!isNull(onConfirm)) {
				onConfirm(paramsForConfirm);
			}
			$('#warning-modal').modal('hide');
			$('#warning-modal').remove();
		});
	},
	WS : function(type) {
		showModalWait();
		var cVect = [];
		var categories = $.LogCollector.categories;
		for ( var cat in categories) {
			if (categories[cat] == true)
				cVect.push(cat);
		}
		switch (type) {
		case 'status':
			$.LogCollector.setStatusToIdle();

			getValues($.LogCollector.logLink + '/status', function(data) {

				var dataVect = data.data.split(' ');
				var st = dataVect[0];

				$(dataVect).each(function(idx) {
					if (idx != 0) {
						$.LogCollector.categories[this] = true;
						$('#' + this + '_on_off').bootstrapSwitch('state', true);
					}
				});
				if (st == 'start' || st == 'resume') {
					$.LogCollector.setStatusToPause();

				} else if (st == 'pause') {
					$.LogCollector.setStatusToResume();
				} else if (st == 'stop') {
					$.LogCollector.setStatusToStopped();
				}

				hideModalWait();
			}, function() {
				hideModalWait();
			});
			break;

		case 'start':
			postValues($.LogCollector.logLink + '/start', cVect, function(data) {
				hideModalWait();
				$.LogCollector.setStatusToPause();
			}, function() {
				hideModalWait();
			});
			break;

		case 'stop':
			getValues($.LogCollector.logLink + '/stop', function(data) {
				hideModalWait();
				$.LogCollector.setStatusToStopped();
			}, function() {
				hideModalWait();
			});
			break;

		case 'resume':
			postValues($.LogCollector.logLink + '/resume', cVect, function(data) {
				hideModalWait();
				$.LogCollector.setStatusToPause();
			}, function() {
				hideModalWait();
			});
			break;

		case 'pause':
			getValues($.LogCollector.logLink + '/pause', function(data) {
				$.LogCollector.setStatusToResume();
				hideModalWait();
			}, function() {
				hideModalWait();
			});
			break;

		case 'delete':
			deleteValues($.LogCollector.logLink + '/collectedLogs', function(data) {
				hideModalWait();
				$.LogCollector.setStatusToIdle();
			}, function() {
				hideModalWait();
			});
			break;

		default:
			hideModalWait();
			break;
		}
	}
};
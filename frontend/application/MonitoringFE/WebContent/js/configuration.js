var _CONF_SERVICES = '/fe';

function progressDescriptionBlock(current, max, targetDom, title, border) {
	border = border || '';
	var percent = 0;
	if (max > 0) {
		percent = Math.round((current / max) * 10000) / 100;
	}
	var color = 'text-green';
	if (percent >= 50 && percent < 80) {
		color = 'progress-bar-light-blue';
	}
	if (percent >= 80 && percent < 90) {
		color = 'text-yellow';
	}
	if (percent >= 90) {
		color = 'text-red';
	}
	
	var content = '';
	content += '<div class="description-block '+border+'">';	
	content += '<h4 class="description-header">'+current+'</h4>';
	content += '<span class="description-text">'+title+'</span><br>';
	content += '<span class="description-percentage '+color+'">'+percent+'%</span>';
	content += '</div>';
	$(targetDom).html(content);
}

function resourceUsageProgress(current, max, targetDom, title) {
	var percent = 0;
	if (max > 0) {
		percent = Math.round((current / max) * 10000) / 100;
	}
	var color = 'progress-bar-green';
	if (percent >= 50 && percent < 80) {
		color = 'progress-bar-light-blue';
	}
	if (percent >= 80 && percent < 90) {
		color = 'progress-bar-yellow';
	}
	if (percent >= 90) {
		color = 'progress-bar-red';
	}

	var content = '';
	content += '<div class="progress-group">';
	content += '<span class="progress-text">' + title + '</span>';
	content += '<span class="progress-number"><b>' + current + '</b>/' + max + '</span>';
	content += '<div class="progress sm">';
	content += '<div class="progress-bar ' + color + '" style="width: ' + percent + '%"></div>';
	content += '</div>';
	content += '</div>';
	$(targetDom).html(content);
}

/* Services */
function WS_getInventoryService(serviceName, handler, targetDom) {
	getValues(_BASE_WEB_ROOT + '/inventoryService/' + mediaIfcName, handler, null, targetDom);
}
function WS_getList(handler, targetDom) {
	getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/mediaInterface', handler, null, targetDom);
}
function WS_createMediaIfc(inputData, handler, targetDom) {
	putValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/mediaInterface', inputData, handler, null, targetDom,
			'modal-body');
}
function WS_deleteMediaIfc(mediaIfcName, handler, targetDom) {
	deleteValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/mediaInterface/' + mediaIfcName, handler, null, targetDom,
			'modal-body');
}
function WS_updateMediaIfc(inputData, handler, targetDom) {
	postValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/mediaInterface', inputData, handler, null, targetDom,
			'modal-body');
}
function WS_getMediaIfcTemplate(handler, targetDom) {
	getValues(_BASE_WEB_ROOT + _CONF_SERVICES + '/mediaInterface/template', handler, null, targetDom);
}

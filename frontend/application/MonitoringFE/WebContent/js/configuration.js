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


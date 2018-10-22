function customTab(idprefix, title, tabtitles, tabcontents) {
	var customtab = '';
	customtab += '<div class="nav-tabs-custom">'; // nav tabs
	customtab += '<ul class="nav nav-tabs pull-right">';
	$(tabtitles).each(
			function(tidx) {
				var tclass = '';
				var tabid = '#' + idprefix + '_tab_' + tidx;
				var texp = 'false';
				if (tidx == 0) {
					tclass = 'active';
					texp = 'true';
				}
				customtab += '<li class="' + tclass + '"><a href="' + tabid + '" data-toggle="tab" aria-expanded="'
						+ texp + '">' + this + '</a></li>';
			});
	customtab += '<li class="pull-left header">' + title + '</li></ul>';
	customtab += '<div class="tab-content">';
	$(tabcontents).each(function(cidx) {
		var cclass = '';
		var contentid = idprefix + '_tab_' + cidx;
		if (cidx == 0) {
			cclass = 'active';
		}
		customtab += '<div class="tab-pane ' + cclass + '" id="' + contentid + '">';
		customtab += this;
		customtab += '</div>';
	});
	customtab += '</div>';
	customtab += '</div>';

	return customtab;
}

function widgetBox(title, content, footer, color, bodytype) {
	color = color || 'default';
	bodytype = bodytype || '';
	var box = '';
	box += '<div class="box box-' + color + '">';
	box += '<div class="box-header with-border">';
	box += title;
	box += '</div>';
	box += '<div class="box-body ' + bodytype + '">';
	box += content;
	box += '</div>';
	box += '<div class="box-footer">';
	box += footer;
	box += '</div>';
	box += '</div>';

	return box;
}

function drawBox(title, content, footer, toolbox, boxclasses) {
	toolbox = toolbox || '';
	footer = footer || null;
	var box = '';

	box += '<div class="box ' + boxclasses + '">';
	box += '<div class="box-header" data-toggle="tooltip" title="Header tooltip">';
	box += '<h3 class="box-title">' + title + '</h3>';
	box += '<div class="box-tools pull-right">';
	box += toolbox;
	box += '</div>';
	box += '</div>';
	box += '<div class="box-body">';
	box += content;
	box += '</div>';
	if (footer != null) {
		box += '<div class="box-footer">' + footer + '</div>';
	}
	box += '</div>';

	return box;
}

function drawTableBox(title, content, footer, toolbox, boxclasses) {
	toolbox = toolbox || '';
	footer = footer || null;
	var box = '';

	box += '<div class="box ' + boxclasses + '">';
	box += '<div class="box-header" data-toggle="tooltip" title="Header tooltip">';
	box += '<h3 class="box-title">' + title + '</h3>';
	box += '<div class="box-tools pull-right">';
	box += toolbox;
	box += '</div>';
	box += '</div>';
	box += '<div class="box-body no-padding">';
	box += content;
	box += '</div>';
	if (footer != null) {
		box += '<div class="box-footer">' + footer + '</div>';
	}
	box += '</div>';

	return box;
}

function confirmDialog(dialogid, title, body) {
	title = title || "Warning: are you sure you want to delete?";
	body = body || '';
	if (body.length > 0) {
		body = '<div class="modal-body"><p>' + body + '</p></div>';
	}

	var confirm = '';
	confirm += '<div class="modal" id="' + dialogid + '">';
	confirm += '<div class="modal-dialog">';
	confirm += '<div class="modal-content">';
	confirm += '<div class="modal-header warning">';
	confirm += '<button type="button" class="close" data-dismiss="modal">';
	confirm += '<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>';
	confirm += '</button>';
	confirm += '<h4 class="modal-title text-left"><span class="glyphicon glyphicon-warning-sign"> </span> ' + title
			+ '</h4>';
	confirm += '</div>';
	confirm += body;
	confirm += '<div class="modal-footer">';
	confirm += '<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>';
	confirm += '<button type="button" class="btn btn-primary btn-confirm" data-dismiss="modal">Confirm</button>';
	confirm += '</div>';
	confirm += '</div>';
	confirm += '</div>';
	confirm += '</div>';
	return confirm;
}

function showModalWait() {
	$('#pleaseWaitDialog').modal('show');
}

function hideModalWait() {
	$('#pleaseWaitDialog').modal('hide');
}

function tableDef(rows) {
	var table = '<table class="table table-hover table-definition"><tbody>';
	$(rows).each(function() {
		table += this;
	});
	table += '</tbody></table>';
	return table;
}

function tableRowDef(name, value) {
	return '<tr><td><strong>' + name + '</strong></td><td>' + value + '</td></tr>';
}

function boxPrimary(title, content, solid, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-primary');
}

function solidBoxSuccess(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-success box-solid');
}

function solidBoxWarning(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-warning box-solid');
}

function solidBoxError(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-danger box-solid');
}

function solidBoxPrimary(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-primary box-solid');
}

function solidBoxInfo(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-info box-solid');
}

function solidBoxDefault(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-default box-solid');
}

function condensedBoxWarning(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-warning box-condensed');
}

function condensedBoxPrimary(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-primary box-condensed');
}

function condensedBoxSuccess(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-success box-condensed');
}

function condensedBoxError(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-danger box-condensed');
}

function condensedBoxInfo(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-info box-condensed');
}

function condensedBoxDefault(title, content, footer, toolbox) {
	return drawBox(title, content, footer, toolbox, 'box-default box-condensed');
}

function condensedTableBox(title, content, footer, toolbox) {
	return drawTableBox(title, content, footer, toolbox, 'box-condensed');
}

function condensedTableBoxPrimary(title, content, footer, toolbox) {
	return drawTableBox(title, content, footer, toolbox, 'box-primary box-condensed');
}

function calloutInfo(title, content) {
	return '<div class="callout callout-info"><h4>' + title + '</h4><p>' + content + '</p></div>';
}

function calloutWarning(title, content) {
	return '<div class="callout callout-warning"><h4>' + title + '</h4><p>' + content + '</p></div>';
}

function condensedRemovableBoxWarning(title, content, footer) {
	return drawBox(
			title,
			content,
			footer,
			'<button class="btn btn-primary btn-xs" onclick="javascript:$(this).parent().parent().parent().remove();"><i class="fa fa-times"></i></button>',
			'box-warning box-condensed');
}

function boxLabel(dt, dd) {
	return '<dl class="dl-horizontal dl-box-label"><dt>' + dt + '</dt><dd>' + dd + '</dd></dl>';
}

function buttonAddPrimary(targetObject) {
	return '<a class="btn btn-primary btn-flat" href="configuration.jsp?operation=create&object=' + targetObject
			+ '" role="button"><span class="glyphicon glyphicon-plus"></span> Add</a>';
}

function breadcrumbList(objTarget, title) {
	return '<a href="configuration.jsp?operation=list&object=' + objTarget + '">' + title
			+ '</a> <span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span> ';
}

function breadcrumbRef(pageTarget, title) {
	return '<a href="' + pageTarget + '">' + title
			+ '</a> <span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span> ';
}

function setPageTitle(title, subtitle) {
	subtitle = subtitle || '';
	$('#main-title').html(title + "<small>" + subtitle + "</small>");
}

function setActiveMenu(menu_id) {
	var menu = $('#' + menu_id);
	$('.sidebar-menu .menu-link').removeClass('menu-active').removeClass('active');
	$('.sidebar-menu ul.treeview-menu').removeClass('menu-open').hide();
	
	if (menu.hasClass('treeview')) {
		menu.children("a").first().click();
	} else {
		
		var treeroot = menu.parent().parent();
		if (treeroot.hasClass('treeview')) {
			treeroot.children("a").first().click();
			if(treeroot.parent().parent().hasClass('treeview')){
				treeroot.parent().parent().children("a").first().click();
			}
		}
	}
	menu.addClass('menu-active').addClass('active');
}

function drawKnob(targetDom, label, current, max, warn_threshold, red_threshold, width, height) {
	width = width || 90;
	height = height || 90;
	warn_threshold = warn_threshold || 1;
	red_threshold = red_threshold || 10;

	var percent = 0;
	var colorclass = "#00a65a";
	var dataNA = false;
	if (max > 0) {
		percent = Math.ceil(current * 100 / max);
	} else {
		colorclass = "#ccc";
		dataNA = true;
	}

	if (percent > warn_threshold)
		colorclass = "#f39c12";
	if (percent > red_threshold)
		colorclass = "#dd4b39";

	var content = '';
	content += '<div style="display: inline; width: ' + width + 'px; height: ' + height + 'px;">';
	content += '<input type="text" class="knob" value="' + (100 - percent) + '" data-width="' + width
			+ '" data-height="' + height + '" data-fgcolor="' + colorclass + '" data-skin="tron" data-na="' + dataNA
			+ '" data-thickness=".2" data-readOnly="true" data-displayPrevious="true">';
	content += '</div>';
	content += '<div class="knob-label">' + label + '</div>';

	$(targetDom).html(content);

	$(targetDom).find(".knob")
			.knob(
					{
						format : function(v) {
							return v + '%';
						},
						draw : function() {
							// "tron" case
							if (this.$.data('skin') == 'tron') {

								var a = this.angle(this.cv) // Angle
								, sa = this.startAngle // Previous start angle
								, sat = this.startAngle // Start angle
								, ea // Previous end angle
								, eat = sat + a // End angle
								/* , r = true */;

								this.g.lineWidth = this.lineWidth;

								this.o.cursor && (sat = eat - 0.3) && (eat = eat + 0.3);

								if (this.o.displayPrevious) {
									ea = this.startAngle + this.angle(this.value);
									this.o.cursor && (sa = ea - 0.3) && (ea = ea + 0.3);
									this.g.beginPath();
									this.g.strokeStyle = this.previousColor;
									this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, sa, ea, false);
									this.g.stroke();
								}

								this.g.beginPath();
								this.g.strokeStyle = this.o.bgColor;
								this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, sat, eat, false);
								this.g.stroke();

								this.g.beginPath();
								this.g.strokeStyle = this.o.fgColor;
								this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, eat - 0.00001,
										sat - 0.00001, false);
								this.g.stroke();

								this.g.lineWidth = 2;
								this.g.beginPath();
								this.g.strokeStyle = this.o.fgColor;
								this.g.arc(this.xy, this.xy, this.radius - this.lineWidth + 1 + this.lineWidth * 2 / 3,
										0, 2 * Math.PI, false);
								this.g.stroke();

								return false;
							}
						}
					});

}

function indicatorWidget2(targetDom, count, title, bgclass, iconclass, warn_threshold, red_threshold, link, max_value) {
	var target = $(targetDom);
	var colorclass = bgclass;
	target.empty();
	if (warn_threshold != null || red_threshold != null) {
		if (count > warn_threshold)
			colorclass = "bg-yellow";
		if (count > red_threshold)
			colorclass = "bg-red";
	}
	var progressbarvalue = 100;
	if (max_value != null) {
		progressbarvalue = Math.floor(count * 100 / max_value);
	}
	var content = '';
	content += '<div class="info-box ' + colorclass + '">';
	content += '<a href="' + link + '"><span class="info-box-icon"><i class="ion ' + iconclass + '"></i></span></a>';
	content += '<div class="info-box-content">';
	content += '<span class="info-box-text">' + title + '</span> <span class="info-box-number">' + count + '</span>';
	content += '<div class="progress"><div class="progress-bar" style="width: ' + progressbarvalue + '%"></div></div>';
	content += '<span class="progress-description"><a href="' + link + '">View details</a></span>';
	content += '</div>';
	content += '</div>';

	$(target).html(content);
}

function customWidget(targetDom, number, title, bgclass, iconclass, link) {
	var target = $(targetDom);
	var colorclass = bgclass;
	
	var content = '';
	content += '<div class="info-box">';
	content += '<a href="' + link + '"><span class="info-box-icon ' + colorclass + '"><i class="ion ' + iconclass + '"></i></span></a>';
	content += '<div class="info-box-content">';
	content += '<span class="info-box-big-number">' + number + '</span> <span class="info-box-text">' + title + '</span>';
	content += '</div>';
	content += '</div>';

	$(target).html(content);
}

function indicatorWidget(targetDom, count, title, bgclass, iconclass, warn_threshold, red_threshold, link) {
	var target = $(targetDom);
	var colorclass = bgclass;
	target.empty();
	if (warn_threshold != null || red_threshold != null) {
		if (count > warn_threshold)
			colorclass = "bg-yellow";
		if (count > red_threshold)
			colorclass = "bg-red";
	}
	var content = '';
	content += '<div class="tw-small-box small-box ' + colorclass + '">';
	content += '<div class="inner">';
	content += '<h3>' + count + '</h3>';
	content += '<p>' + title + '</p>';
	content += '</div>';
	content += '<div class="icon"><i class="ion ' + iconclass + '"></i></div>';
	content += '<a href="' + link + '" class="small-box-footer"> View <i class="fa fa-arrow-circle-right"></i></a>';
	content += '</div>';

	$(target).html(content);
}

function statsIndicatorWidget(targetDom, count, title, link) {
	indicatorWidget(targetDom, count, title, 'bg-aqua', 'ion-stats-bars', null, null, link);
}

function counterIndicatorWidget(targetDom, count, title, link, warn_threshold, red_threshold) {
	indicatorWidget(targetDom, count, title, 'bg-green', 'ion-pie-graph', warn_threshold, red_threshold, link);
}

function definition(name, value) {
	if (isNull(value)) {
		value = '-';
	}
	return '<dt>' + name + '</dt><dd>' + value + '</dd>';
}

function definitionList(content, horizontal) {
	if (isNull(horizontal)) {
		horizontal = true;
	}
	var co = "";
	if (horizontal) {
		co = ' class="dl-horizontal dl-nms"';
	}

	return '<dl' + co + '>' + content + '</dl>';
}

function alertWarning(msg, title) {
	title = title || 'Warning: ';
	return '<div class="alert alert-warning"><strong><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span> ' + title + '</strong>' + msg + '</div>';
}

function alertDanger(msg) {
	return '<div class="alert alert-danger">' + msg + '</div>';
}

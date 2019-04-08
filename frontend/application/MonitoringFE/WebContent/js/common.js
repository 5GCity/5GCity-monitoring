var _BASE_WEB_ROOT = '/FrontEnd/rest';
var _CONF_SERVICES = '/fe';
var _UM_SERVICES = '/um';
var _UMSO_SERVICES = '/umso';
var CONSOLE_DEBUG = false;
var NULL_TOKEN = '#NULL#';
var subnets = {};

function getCurrentDateTime() {
	var d = new Date();
	var curr_day = d.getDate();
	var curr_month = d.getMonth();
	var curr_year = d.getFullYear();
	var curr_hour = d.getHours();
	var curr_min = d.getMinutes();
	var curr_sec = d.getSeconds();
	var curr_msec = d.getMilliseconds();
	return curr_day + "_" + curr_month + "_" + curr_year + "-" + curr_hour + "_" + curr_min + "_" + curr_sec + "_"
			+ curr_msec;
}

function isValidIpFormat(ip) {
	var validIP = true;
	var split = ip.split('.');
	if (split.length != 4) {
		validIP = false;
	} else {
		validIP = true;
		for (var i = 0; i < split.length; i++) {
			var s = split[i];
			if (s.length == 0 || isNaN(s) || s < 0 || s > 255)
				validIP = false;
		}
	}
	return validIP;
}

function bindWarningInput(inputElement, warningMessage, isSelectWarning) {
	
	if(isNull(inputElement)){
		return;
	}
	
	var par = $(inputElement).parent();// $(inputElement).parents("div.form-group");//
	if (par.hasClass("has-feedback") && par.hasClass("has-warning"))
		return;
	var iconMargin = '';
	if (isSelectWarning) {
		iconMargin = 'style="margin-right: 12px;"';
	}
	var totHelpBlock = $('.help-block').length || 0;
	totHelpBlock += 1;
	var inputWarningId = 'inputWarning_' + totHelpBlock;
	var wrnspan = '<span class="glyphicon glyphicon-warning-sign form-control-feedback"' + iconMargin
			+ ' aria-hidden="true"></span>\
	<span id="' + inputWarningId
			+ '" class="sr-only">(warning)</span>\
	<span id="helpBlock_' + totHelpBlock + '" class="help-block">'
			+ warningMessage + '</span>';

	par.addClass("has-warning has-feedback").append(wrnspan);
	$(inputElement).attr("aria-describedby", inputWarningId);

	if (isSelectWarning) {
		$(inputElement).addClass('select-warning');
		var label = par.find('label');
		$(label).addClass('select-warning-label');
	}
}
function unbindWarningInput(inputElement, isSelectWarning) {
	if(isNull(inputElement)){
		return;
	}
	var par = $(inputElement).parent();// $(inputElement).parents("div.form-group");//
	if (par.hasClass("has-feedback") && par.hasClass("has-warning")) {
		par.removeClass("has-warning has-feedback");
		par.find("span.glyphicon-warning-sign").remove();
		par.find("span.sr-only").remove();
		par.find("span.help-block").remove();
		$(inputElement).removeAttr("aria-describedby");

		if (!isSelectWarning)
			return;
		var label = $(inputElement).parent().find('label');
		$(label).removeClass('select-warning-label');
		$(inputElement).removeClass('select-warning ');
	}
}

function toastAutocompleteOffForIpV6(selectedObj, subnets, input) {
	if (subnets == null || selectedObj == null || input == null || subnets.length == 0) {
		$('#main-container').find('#toastIPV6').remove();
		return;
	}

	for (var i = 0; i < subnets.length; i++) {
		var obj = subnets[i];
		if (obj.name == selectedObj.val()) {
			var networkPlanType = obj.networkPlanType;
			if (networkPlanType == 'IPV6' && $('#toastIPV6').length == 0) {
				var style = 'style="z-index: 100; display: none; color: #a9a;"';
				var toast = '<div id="toastIPV6"' + style + ' >' + 'autocomplete off for IPV6 </div>';
				$(toast).insertAfter(input).fadeIn(500);
			} else if (networkPlanType == 'IPV4') {
				$('#main-container').find('#toastIPV6').fadeOut(500, function() {
					$('#main-container').find('#toastIPV6').remove();
				});
			}
			break;
		}
	}
}

function splitByUpperCase(stringa) {
	if (isNull(stringa)) {
		return '';
	}
	var str = stringa[0].toUpperCase() + stringa.substring(1);
	str = str.replace(/([a-z])([A-Z])/g, "$1 $2");
	return str.replace(/([A-Z])([A-Z][a-z])/g, "$1 $2");
	// var v = str.split(/(?=[A-Z])/).join(" ");
	// return v;
};

function setReadOnlyForm(targetDom) {
	$(targetDom).find('input').prop('readonly', true);
	$(targetDom).find('select').prop('disabled', true);
	$(targetDom).find('.btn').hide();
	$(targetDom).find('.hidable').hide();
}

function getValues(targetUrl, successHandler, errorHandler, targetDom, overlayDom) {
	callAsync('GET', targetUrl, null, successHandler, errorHandler, targetDom, overlayDom);
}

function postValues(targetUrl, input, successHandler, errorHandler, targetDom, overlayDom) {
	callAsync('POST', targetUrl, input, successHandler, errorHandler, targetDom, overlayDom);
}

function putValues(targetUrl, input, successHandler, errorHandler, targetDom, overlayDom) {
	callAsync('PUT', targetUrl, input, successHandler, errorHandler, targetDom, overlayDom);
}

function deleteValues(targetUrl, successHandler, errorHandler, targetDom, overlayDom, input) {
	input = input || null;
	callAsync('DELETE', targetUrl, input, successHandler, errorHandler, targetDom, overlayDom);
}

function callAsync(method, targetUrl, input, successHandler, errorHandler, targetDom, overlayDom) {

	var inputData = '';
	var overlay = $('<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>');

	if (input != null) {
		if (typeof input != "string") {
			inputData = JSON.stringify(input);
		} else {
			inputData = input;
		}
	}
	overlayDom = overlayDom || false;
	errorHandler = errorHandler || null;

	if (overlayDom) {
		if (overlayDom === 'modal-body') {
			showModalWait();
		} else {
			$(overlayDom).append(overlay);
		}
	}

	$('.display-message').empty();
	var spanRemove = '<span class="fa fa-times pull-right" onclick="$(this).parent().remove();" style="cursor:pointer;"></span>';
	$.ajax({
		url : targetUrl,
		type : method,
		contentType : 'application/json',
		dataType : 'json',
		data : inputData,
		cache : true,
		success : function(data) {
			if (data.code != 200) {
				if (errorHandler == null) {
					$('.display-message').each(function() {
						$(this).html('<div class="alert alert-danger">' + data.message + spanRemove + '</div>');
					});
				} else {
					errorHandler(data.code, data.message, targetDom);
				}
			} else {
				successHandler(data, targetDom);
			}
		},
		error : function(obj, status, text) {
			if (errorHandler == null) {
				$('.display-message').each(
						function() {
							$(this).html(
									'<div class="alert alert-danger">' + text + ":" + obj.responseText + spanRemove
											+ '</div>');
						});
			} else {
				errorHandler(500, text + ":" + obj.responseText, targetDom);
			}
		},
		complete : function() {
			if (overlayDom) {
				if (overlayDom === 'modal-body') {
					hideModalWait();
				} else {
					$(overlayDom).find(overlay).remove();
				}
			}
		},
		async : true,
	});
}

function zeroPad(number, len) {
	var n = '';
	var nl = String(number).length;
	for (var l = 0; l < len - nl; l++) {
		n = '0' + n;
	}
	return n + number;
}

function formatDate(date) {
	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	var hour = date.getHours();
	var minutes = date.getMinutes();
	var seconds = date.getSeconds();
	return zeroPad(day, 2) + '/' + zeroPad(month, 2) + '/' + year + ' ' + zeroPad(hour, 2) + ':' + zeroPad(minutes, 2)
			+ ':' + zeroPad(seconds, 2);
}

function getURLParameter(sParam) {
	var sPageURL = window.location.search.substring(1);
	var sURLVariables = sPageURL.split('&');
	for (var i = 0; i < sURLVariables.length; i++) {
		var sParameterName = sURLVariables[i].split('=');
		if (sParameterName[0] == sParam) 
			return sParameterName[1];
	}
}

function getDashBoardURL(sParam) {
	var sPageURL = window.location.search.substring(1);

	var sParameterName = sPageURL.split('=');
	var sParameterTot = "";
	if (sParameterName[0] == sParam) {
		for (var i = 1; i < sParameterName.length; i++) {
			sParameterTot += sParameterName[i];
			if(i < (sParameterName.length-1)) {
			  sParameterTot += '=';
			}
		}
		return sParameterTot;
	}
}

function isNull(value) {
	return (typeof value == 'undefined' || value == null || '' + value === '');
}

function selectOptions(name, selection, options) {
	var content = '<select class="form-control input-sm" name="' + name + '">';
	var selected = '';
	$(options).each(
			function() {
				if (('' + selection) === ('' + this.value) || (isNull(selection) && ('' + this.type) === 'DEFAULT')) {
					selected = 'selected';
				} else {
					selected = '';
				}
				content += '<option value="' + this.value + '" data-type="' + this.type + '" ' + selected + '>'
						+ this.label + '</option>';
			});
	content += '</select>';
	return content;
}

function isUnknow(val, dynamicValues) {
	var isNotKnown = true;
	if (isNull(val))
		return isNotKnown;
	$(dynamicValues).each(function() {
		if (this.value == val) {
			isNotKnown = false;
			return false;
		}
	});
	return isNotKnown;
}

function dynamicSelect(name, currentvalue, dynamicValues, removeButton, readOnly, extraParams) {
	removeButton = removeButton || false;
	readOnly = readOnly || false;
	extraParams = extraParams || '';
	var disabled = '';
	if (readOnly)
		disabled += ' disabled ';
	var content = '<select ' + disabled + ' class="form-control input-sm data-input" name="' + name + '" '
			+ extraParams + '>';

	var groupedDynamicValues = new Array();
	groupedDynamicValues['NOGROUP'] = [];

	$(dynamicValues).each(function() {
		var groupName = 'NOGROUP';
		if (!isNull(this.group)) {
			groupName = this.group;
		}
		if (isNull(groupedDynamicValues[groupName])) {
			groupedDynamicValues[groupName] = [];
		}
		groupedDynamicValues[groupName].push(this);
	});

	// var initCurrValue = currentvalue;
	for ( var groupName in groupedDynamicValues) {
		var values = groupedDynamicValues[groupName];
		if (groupName != 'NOGROUP') {
			content += '<optgroup label="' + groupName + '">';
		}
		$(values).each(
				function(i) {
					var selected = '';
					var dataIndex = '';
					var disabled = '';
					// if (isNull(initCurrValue) && i == 0) {
					// currentvalue = this.value;
					// initCurrValue = currentvalue;
					// } else if (i != 0) {
					// currentvalue = initCurrValue;
					// }
					if (('' + currentvalue) === ('' + this.value)
							|| (isNull(currentvalue) && '' + this.type === 'DEFAULT')
							|| (isUnknow(currentvalue, dynamicValues) && '' + this.type === 'DEFAULT')) {
						selected = 'selected';
					}
					if (!isNull(this.index) && this.index >= 0) {
						dataIndex = ' data-index="' + this.index + '"';
					}
					if (!isNull(this.disabled) && this.disabled) {
						disabled = ' disabled';
					}
					content += '<option value="' + this.value + '" data-type="' + this.type + '" ' + selected
							+ dataIndex + disabled + '>' + this.label + '</option>';
				});
		if (groupName != 'NOGROUP') {
			content += '</optgroup>';
		}
	}
	// $(dynamicValues).each(
	// function() {
	// if ('' + currentvalue === '' + this.value || (isNull(currentvalue) && ''
	// + this.type === 'DEFAULT')) {
	// selected = 'selected';
	// } else {
	// selected = '';
	// }
	// if (!isNull(this.index) && this.index >= 0) {
	// dataIndex = ' data-index="' + this.index + '"';
	// }
	// content += '<option value="' + this.value + '" data-type="' + this.type +
	// '" ' + selected + dataIndex
	// + '>' + this.label + '</option>';
	// });
	content += '</select>';
	if (removeButton) {
		content = '<div class="input-group" name="' + name + '">' + content;
		content += '<span class="input-group-btn"><button class="btn btn-default btn-flat btn-sm" onclick="javascript:$(this).parent().parent().parent().trigger(\'removeitem\');$(this).parent().parent().remove();" type="button"><span class="glyphicon glyphicon-minus"></span></button></span>';
		content += '</div>';
	}
	return content;
}

function dynamicSelectArray(name, currentvalues, dynamicValues) {
	var content = '';
	$(currentvalues).each(function() {
		content += dynamicSelect(name, this, dynamicValues, true);
	});
	return content;
}

function booleanSelect(name, value, labels, addons) {
	labels = labels || [ 'true', 'false' ];
	addons = addons || '';
	var content = '<select class="form-control input-sm data-input" name="' + name + '"  ' + addons + '>';
	if (value === true || value === 'true') {
		content += '<option value="true" selected>' + labels[0] + '</option>';
		content += '<option value="false">' + labels[1] + '</option>';
	} else {
		content += '<option value="true">' + labels[0] + '</option>';
		content += '<option value="false" selected>' + labels[1] + '</option>';
	}
	content += '</select>';
	return content;
}

function booleanSelect(name, value, labels, addons, readonly) {
	readonly = readonly || false;
	labels = labels || [ 'true', 'false' ];
	addons = addons || '';

	var disabled = '';
	if (readonly) {
		disabled = ' disabled';
	}

	var content = '<select class="form-control input-sm data-input" ' + disabled + ' name="' + name + '"  ' + addons
			+ '>';
	if (value === true || value === 'true') {
		content += '<option value="true" selected>' + labels[0] + '</option>';
		content += '<option value="false">' + labels[1] + '</option>';
	} else {
		content += '<option value="true">' + labels[0] + '</option>';
		content += '<option value="false" selected>' + labels[1] + '</option>';
	}
	content += '</select>';
	return content;
}

function addInputTextToArray(element, name) {
	addInputToArray(element, name, "TEXT");
}

function addInputNumberToArray(element, name) {
	addInputToArray(element, name, "NUMBER");
}

function addInputToArray(element, name, inputType) {
	var content = '';
	content += '<div class="input-group input-array" name="' + name + '">';
	if (inputType == "TEXT") {
		content += inputText(name, '');
	}
	if (inputType == "NUMBER") {
		content += inputNumber(name, '');
	}
	content += '<span class="input-group-btn">' + '<button onclick="javascript:$(this).parent().parent().remove();" '
			+ 'class="btn btn-default btn-flat btn-sm" type="button">'
			+ '<span class="glyphicon glyphicon-minus"></span></button></span>';
	content += '</div>';
	$(element).before('' + content);
}

function inputTextArray(name, values, readonly, addons) {
	return inputArray(name, values, "TEXT", readonly, addons);
}

function inputNumberArray(name, values, readonly, addons) {
	return inputArray(name, values, "NUMBER", readonly, addons);
}

function inputArray(name, values, inputType, readonly, addons) {
	var content = '';
	var addscript = '';
	readonly = readonly || false;
	addons = addons || '';

	if (inputType == "TEXT") {
		addscript = 'javascript:addInputTextToArray($(this),\'' + name + '\');';
	}
	if (inputType == "NUMBER") {
		addscript = 'javascript:addInputNumberToArray($(this),\'' + name + '\');';
	}

	var strHide = '';
	if (readonly) {
		strHide = ' style="display:none" ';
	}

	$(values).each(
			function() {
				content += '<div class="input-group" name="' + name + '">';
				if (inputType == "TEXT") {
					content += inputText(name, this, false, readonly, addons);
				}
				if (inputType == "NUMBER") {
					content += inputNumber(name, this, false, readonly, addons);
				}
				content += '<span class="input-group-btn">' + '<button ' + strHide
						+ 'onclick="javascript:$(this).parent().parent().remove();"'
						+ ' class="btn btn-default btn-flat btn-sm" type="button">'
						+ '<span class="glyphicon glyphicon-minus"></span></button></span>';
				content += '</div>';
			});
	content += '<button type="button" class="btn btn-default btn-flat btn-sm btn-block btn-add" ' + strHide
			+ 'onclick="' + addscript + '"><span class="glyphicon glyphicon-plus"></span> Add</button>';
	return content;
}

function inputTextFixedArray(name, values, labels, readonly, addons) {
	var content = '';
	$(values).each(function(idx) {
		content += '<div class="form-group" name="' + name + '">';
		if (labels != null && labels[idx] != null) {
			content += '<label>' + labels[idx] + '</label>';
		}
		content += inputText(name, this, false, readonly, addons);
		content += '</div>';
	});
	return content;
}

function getInputCursorPosition(input) {
	var val = $(input).val();
	return val.slice(0, input.selectionStart).length;
};

function regExNotSatisfied(stringa, regTemp) {
	var res = true;
	for ( var i in stringa) {
		if (!regTemp.test(stringa[i])) {
			res = false;
			break;
		}
	}
	return res;
}

function checkRegex(inputTocheck) {
	if ($(inputTocheck).attr('name') == 'ip' || $(inputTocheck).attr('name') == 'ipAddress'
			|| !isNull($(inputTocheck).attr('data-inputmask'))) {
		return;
	}
	$(inputTocheck).addClass("check-regex");
	unbindWarningInput(inputTocheck);
	var e = window.event;

	var dnv = $(inputTocheck).parent('[dnv]').length > 0 ? true : false;
	var allowedChar = dnv ? '_\.\-' : '_\.@\-';
	var wMsg = 'You attempted to insert a not allowed character: \' <strong style="color : #777;">' + e.key
			+ '</strong> \'.';
	// e = e || window.event;
	var regDiv = $(".regex-parameter-selector");
	if (($(regDiv).length == 0 || $(regDiv).find(".check-regex").length == 0) && e.keyCode == 220
			&& !$(inputTocheck).is('[regex-selector]')) {
		// blocco il back Slash
		e.preventDefault();
		bindWarningInput(inputTocheck, wMsg);
	} else if ($(inputTocheck).attr('name') && ($(inputTocheck).attr('name').indexOf('name') != -1)
			|| $(inputTocheck).attr('name').indexOf('Name') != -1) {

		var regString = dnv ? /[a-zA-Z0-9_\.\-]/ : /[a-zA-Z0-9_\.@\-]/;
		var regStringPlus = dnv ? /[a-zA-Z0-9_\.\-]/g : /[a-zA-Z0-9_\.@\-]/g;
		wMsg += ' The allowed characters are \'<strong style="color : #777;">' + allowedChar + '</strong>\'';
		if (e.type != 'paste') {
			// var k = e.key;
			//			
			$(inputTocheck)
					.keyup(
							function(evt) {
								var isWarning = !regExNotSatisfied(evt.key, regString); // evt.key.search(/[^a-z0-9_.@-]+/i)
								// >= 0;
								if (isWarning) {
									var v = $(inputTocheck).val();
									wMsg = 'You attempted to insert a not allowed character: \' <strong style="color : #777;">'
											+ evt.key
											+ '</strong> \'. \
    					The allowed characters are \'<strong style="color : #777;">'
											+ allowedChar + '</strong>\'';

									bindWarningInput(inputTocheck, wMsg);
									setTimeout(function() {
										$(inputTocheck).val(v);
									}, 0);
								}
							});
		}
		if (!regExNotSatisfied(e.key, regString)) {
			bindWarningInput(inputTocheck, wMsg);
			e.preventDefault();

		} else if (e.type == 'paste') {
			var oldValue = $(inputTocheck).val();
			if (isNull(oldValue)) {
				// return;
			}

			wMsg = 'You can not paste a string that contains characters \
				different from \'<strong style="color : #777;">'
					+ allowedChar + '</strong>\'';

			e = inputTocheck.onpaste.arguments[0];
			var cursorPos = getInputCursorPosition(inputTocheck);
			var keyIsUp = true;
			$(inputTocheck)
					.keyup(
							function(evt) {
								if (!keyIsUp)
									return;
								var oldLength = oldValue.length;
								var newInputVal = $(inputTocheck).val();

								var pastedVal = newInputVal.substring(cursorPos, newInputVal.length - oldLength
										+ cursorPos)
										|| '';
								var regExResult = regExNotSatisfied(pastedVal, regString);

								if (!regExResult) {// contiene caratteri
									// sbagliati.
									var finalCleanVal = newInputVal.match(regStringPlus);
									finalCleanVal = finalCleanVal || '';
									if (finalCleanVal.length > 0)
										finalCleanVal = finalCleanVal.join().replace(/,/g, '');
									var btnPaste = ' <p> you can remove unallowed characters and paste the clean content. \
    					 <span class="btn btn-xs btn-primary pull-right" \
    					 \clean-content="'
											+ finalCleanVal
											+ '"\
    					 onclick="javscript: var par = $(this).parents(\'.has-warning\').find(\'input\'); unbindWarningInput(par); \
    					 var cleanC = $(this).attr(\'clean-content\'); \
    					 $(par).val(cleanC);">Clean and Paste</span></p>';
									bindWarningInput(inputTocheck, wMsg + btnPaste);
									$(inputTocheck).val(oldValue);
								}
								keyIsUp = false;
								$(inputTocheck).change();
							});
		}
	}
	$(inputTocheck).removeClass("check-regex");
}

function drawInput(name, value, label, inputType, addons) {
	addons = addons || '';
	label = label || false;
	var addClassOnKeydown = isNull(inputType) || inputType != 'number' ? ' onkeydown="checkRegex(this);" onpaste="checkRegex(this);"'
			: '';
	addons += addClassOnKeydown;

	if (value == null)
		value = '';
	var minWidthInputNumberClass = "";
	if (inputType.indexOf("number") > -1)
		minWidthInputNumberClass = 'min-width-input-number';
	var content = '';
	if (label)
		content += '<label>' + label + '</label>';
	content += '<input class="form-control data-input input-sm ' + minWidthInputNumberClass + '" type="' + inputType
			+ '" name="' + name + '" value="' + value + '" ' + addons + '>';
	return content;
}

function inputTextarea(name, value, label, rows, addons) {
	addons = addons || '';
	label = label || false;
	addons += ' onkeydown="checkRegex(this);" onpaste="checkRegex(this);"';

	if (value == null)
		value = '';
	var content = '';
	if (label)
		content += '<label>' + label + '</label>';
	content += '<textarea class="form-control data-input" name="' + name + '" rows="' + rows + '" ' + addons + '>'
			+ value + '</textarea>';
	return content;
}

function disabledInputText(value, label) {
	label = label || false;
	if (value == null)
		value = '';
	var content = '';
	if (label)
		content += '<label>' + label + '</label>';
	content += '<input class="form-control data-input input-sm" type="text" value="' + value + '" readonly="readonly">';
	return content;
}

function inputText(name, value, label, readonly, addons) {
	addons = addons || '';
	if (readonly) {
		addons += ' readonly="readonly"';
	}
	return drawInput(name, value, label, 'text', addons);
}

function inputText_X(name, value, label, addons) {
	addons = addons || '';
	return drawInput(name, value, label, 'text', addons);
}

function inputNumber(name, value, label, readonly) {
	var ro = '';
	if (readonly) {
		ro = 'readonly="readonly"';
	}
	return drawInput(name, value, label, 'number', ro);
}

function inputPassword(name, value, label) {
	return drawInput(name, value, label, 'password');
}

function inputIPMask(name, value, label, addons) {
	addons = addons || '';
	return drawInput(name, value, label, 'text', 'data-inputmask="\'alias\':\'ip\'" data-mask ' + addons);
}

function getSubnetList(fromDB) {
	if (subnets.length != 0 || fromDB)
		return subnets;
	WS_getSubnetList(function(data) {
		subnets = data.data.subnets;
		getSubnetList(true);
	});

}

function removableBlock(callback, inputclass) {
	inputclass = inputclass || '';
	var content = '<div class="row ' + inputclass + '">';
	content += '<div class="col-xs-11">';
	content += callback;
	content += '</div>';
	content += '<div class="col-xs-1"><label>&nbsp;</label>';
	content += '<div><button onclick="javascript:$(this).parent().parent().parent().remove();" class="btn btn-default btn-flat btn-sm pull-right" type="button"><span class="glyphicon glyphicon-minus"></span></button></div>';
	content += '</div>';
	content += '</div>';
	return content;
}

function addBlockButton(elementbuilder, callback) {
	callback = callback || '';
	return '<button type="button" class="btn btn-default btn-sm btn-block btn-add" onclick="javascript:$(this).before('
			+ elementbuilder + ');' + callback + '"><span class="glyphicon glyphicon-plus"></span> Add</button>';
}

function inputHidden(name, value) {
	return '<input class="form-control data-input" type="hidden" name="' + name + '" value="' + value + '">';
}

function debug(msg, context) {
	context = context || 'debug';
	if (CONSOLE_DEBUG) {
		console.log('@' + context + '@ : ' + msg);
	}
}

function buildInput(root, level, inputObject) {
	inputObject = inputObject || {};
	level = level || 1;
	var name;
	var value;

	var currentlevels = $(root).find('.data-input-l' + level);
	var parentToNull = false;
	debug("LEVEL: " + level);
	debug(currentlevels);

	currentlevels.each(function() {
		var currentlevel = $(this);
		var children = currentlevel.find('.data-input-l' + (level + 1));

		debug("CURRENTLEVEL");
		debug(currentlevel);

		if (!$(currentlevel).hasClass('data-input-skip')) {
			var nullable = $(currentlevel).hasClass('data-input-nullable');
			var parentNullable = $(currentlevel).hasClass('data-input-parent-nullable');
			debug("NOT SKIPPING");
			if (children.length == 0) {
				debug("NO CHILDREN");
				debug("CURRENTOBJECT");
				debug(inputObject);
				if ($.isArray(inputObject)) {
					debug("CURRENTOBJECT IS ARRAY");
					// TODO array in array
					var datainput = $(this).find('.data-input');
					var innerObj = {};
					$(datainput).each(
							function() {
								value = null;
								name = $(this).attr('name');
								// console.log("data-input: " +
								// name);
								if ($(this).is("input")) {
									if (($(this).is('[type="checkbox"]'))) {
										if ($(this).is(':checked')) {
											value = $(this).val();
										}
									} else {
										value = ($(this).val() != '') ? $(this).val()
												: ((!nullable && !parentNullable) ? '' : null);
									}
								} else if ($(this).is("textarea")) {
									value = $(this).val();
								} else {
									value = $(this).find(":selected").val();
								}
								if (value == NULL_TOKEN) {
									value = null;
								}
								if (value != null) {
									innerObj[name] = value;
								} else if (nullable) {
									inputObject[name] = value;
								} else if (parentNullable) {
									parentToNull = true;
									var parentlevel = currentlevel.closest('.data-input-l' + (level - 1)).attr(
											'data-inputname');
									if (parentlevel == undefined || parentlevel == null)
										alert("big problem: parent " + '.data-input-l' + (level - 1) + " not found");
								}
							});
					if (!$.isEmptyObject(innerObj)) {
						inputObject.push(innerObj);
					}
				} else {
					var datainput = $(this).find('.data-input');
					datainput.each(function() {
						value = null;
						// console.log(datainput);
						name = $(this).attr('name');
						// console.log(name);
						if ($(this).is("input")) {
							if (($(this).is('[type="checkbox"]'))) {
								if ($(this).is(':checked')) {
									value = $(this).val();
								}
							} else {
								value = ($(this).val() != '') ? $(this).val() : ((!nullable && !parentNullable) ? ''
										: null);
							}
						} else if ($(this).is("textarea")) {
							value = $(this).val();
						} else {
							value = $(this).find(":selected").val();
						}
						if (value == NULL_TOKEN) {
							value = null;
						}
						// console.log(value);
						if (value != null) {
							if (currentlevel.hasClass('data-input-array')) {
								if (typeof inputObject[name] == 'undefined')
									inputObject[name] = [];
								inputObject[name].push(value);
							} else {
								inputObject[name] = value;
							}
						} else if (nullable) {
							inputObject[name] = value;
						} else if (parentNullable) {
							parentToNull = true;
							var parentlevel = currentlevel.closest('.data-input-l' + (level - 1))
									.attr('data-inputname');
							if (parentlevel == undefined || parentlevel == null)
								alert("big problem: parent " + '.data-input-l' + (level - 1) + " not found");
						}
					});
				}
			} else {
				name = currentlevel.attr('data-inputname');
				if (currentlevel.hasClass('data-input-array')) {
					inputObject[name] = buildInput(currentlevel, level + 1, inputObject[name] || []);
				} else if ($.isArray(inputObject)) {
					inputObject.push(buildInput(currentlevel, level + 1, {}));
				} else {
					inputObject[name] = buildInput(currentlevel, level + 1, inputObject[name] || {});
				}
			}
		}
	});
	if (parentToNull)
		return null;
	return inputObject;
}

var Preloader = (/** @constructor */
function($) {
	"use strict";
	/** @memberOf Preloader.__private */
	var cache = {};
	/** @memberOf Preloader.__private */
	var promise = null;
	/** @memberOf Preloader.__private */
	function loadResource(id, url) {
		return $.ajax({
			url : url,
			type : 'GET',
			dataType : 'json',
			cache : true,
			success : function(data) {
				cache[id] = data.data;
			},
			async : true,
		});
	}
	function loadResourceByPost(id, url, body) {
		return $.ajax({
			url : url,
			type : 'POST',
			contentType : 'application/json',
			dataType : 'json',
			data : JSON.stringify(body),
			cache : true,
			success : function(data) {
				cache[id] = data.data;
			},
			async : true,
		});
	}

	return {
		/**
		 * @memberOf Preloader
		 */
		getResource : function(id) {
			return cache[id];
		},
		setResource : function(id, resourceObject) {
			cache[id] = $.extend(true, {}, resourceObject);
		},
		loadCustomResources : function(resources) {
			resources = $.map(resources, function(res) {
				if (res.body) {
					return loadResourceByPost(res.name, res.uri, res.body);
				} else {
					return loadResource(res.name, res.uri);
				}
			});
			return $.when.apply($, resources);
		},
		cacheDump : function() {
			console.log(cache);
		}
	};

}(jQuery));

var _LOGGED_USER_NAME = $('#logged_user').text();
var _LOGGED_USER_ROLE = $('#logged_user').data('userRole');

var userRoles = [ {
	'value' : 'SYSTEM_ADMIN',
	'label' : 'System Administrator',
	'type' : ''
}, {
	'value' : 'ADMIN',
	'label' : 'Administrator',
	'type' : ''
}, {
	'value' : 'USER',
	'label' : 'User',
	'type' : 'DEFAULT'
} ];

var sliceList;

var userlistPageLink = 'user-management.jsp?operation=list&object=user';

function getRoleName() {
	var roleName = '';
	$(userRoles).each(function() {
		if ('' + this.value == '' + _LOGGED_USER_ROLE) {
			roleName = this.label;
		}
	});
	return roleName;
}

function getLoggedUserName() {
	return _LOGGED_USER_NAME;
}

function isSystemAdmin() {
	return _LOGGED_USER_ROLE === "SYSTEM_ADMIN";
}

function isAdmin() {
	return _LOGGED_USER_ROLE === "ADMIN";
}

function isUser() {
	return _LOGGED_USER_ROLE === "USER";
}

function addVisualSecurity() {
	if (isUser()) {
		$('.user-deny-parent').parent().remove();
		$('.user-deny').remove();
	}
	if (isAdmin()) {
		$('.admin-deny').remove();
	}
}

function help(){

}

function updatePage(name, successHandler, targetDom) {
	console.log("updatePage name :",name);
	successHandler = successHandler || displayAssociationUserSlicesPage;
	targetDom = targetDom || $('#main-container');
	console.log(targetDom);
	WS_getUser(name, successHandler, targetDom);
}

function goToListPage() {
	window.location.href = userlistPageLink;
}

function associationPage() {
	console.log("associationPage ");
	WS_updateUser(buildInput($('#main-container')), goToListPage);
	
//	WS_updateUser(buildInput($('#main-container')), function(data) {
//		$('.display-message').each(
//				function() {
//					$(this).html(
//							'<div class="alert alert-success">Changed slices assiciation for user '
//									+ data.data.username + '</div>');
//				});
//		WS_getUserList(showUserList, targetDom);
//	}, targetDom);

//	$.Slice.WS.update(buildInput($('#main-container')), userlistPageLink);
}

function displayAssociationUserSlicesPage(data, targetDom) {
//	console.log("displayAssociationUserSlicesPage targetDom :");
//	console.log(targetDom);
	var user = data.data;
	console.log("displayAssociationUserSlicesPage user.username: ",user.username);
	saveButton('#nav-toolbar', associationPage, user);
	
	cancelButton('#nav-toolbar', userlistPageLink);

	setPageTitle("Update User: " + user.username);
	setActiveMenu('menu_slice');

	associationUserSlices(user, targetDom, true);
}

function associationUserSlices(user, targetDom, update, readonly) {

	console.log(targetDom);
	update = update || false;
	readonly = readonly || false;
	
	var content = '';
	content += '<form role="form">';
	
	var display = '';
	if (update && !readonly) {
		display = 'style="display:none;"';
	} 
	content += '<div class="col-md-3">';
	var boxgeneral = '';
	boxgeneral += '<div class="form-group data-input-l1" ' + display
	+ '><label>Name</label>';
	boxgeneral += inputText('username', user.username,'',true);
	boxgeneral += '</div>';

	content += condensedBoxPrimary('Settings', boxgeneral);	
	content += '</div>';

	var boxSliceList = '';
	boxSliceList += '<div class="form-group data-input-array data-input-l1" data-inputname="slices">';
	boxSliceList += '<div id="edit-slice-list" class="inline-condensed"></div>';
	boxSliceList += '</div>';
	
	content += '<div class="col-md-8">';
	content += condensedBoxPrimary('Slice List', boxSliceList);
	content += '</div>';
	
	content += '</form>';

	$(targetDom).html(content);
	
	adjustSliceList(user, readonly);
	sliceListChange(user);
	
	var inListObj = {};
	var slices = Preloader.getResource('slice').slices;
	$(slices).each(function(){
		var name = this.name;
		if(isNull(inListObj[name]))
			inListObj[name] = this;
	});
	
	$('#edit-slice-list select').change();		
	
	if (readonly) {
		setReadOnlyForm($(targetDom));
	}
	
	help();
}

function adjustSliceList(user, readonly) {
	readonly = readonly || false;
	var content = '';
	if (!readonly) {
		if($(user.slices).length > 0) {
			$(user.slices).each(function(indexInArray, value) {
				content += removableSliceName(user,value.name, 'data-input-l2');
			});
		} else {
			content += removableSliceName(user, null, 'data-input-l2');
		}
		content += '<button type="button" class="btn btn-default btn-sm btn-block btn-add"><span class="glyphicon glyphicon-plus"></span> Add</button>';
	} else {
		$(user.slices).each(function(indexInArray, value) {
			content += removableSliceName(user, value.name, 'data-input-l2', readonly);
		});
	}
	$('#edit-slice-list').html(content);

	if (!readonly) {
		$('#edit-slice-list').find('button.btn-add').click(function() {
			
			$(this).before('' + removableSliceName(user, null, 'data-input-l2'));
			sliceListChange(user);
			
			var selectList = $('#edit-slice-list select');
			var lastSelectAdded = selectList[selectList.length-1];
			$(lastSelectAdded).change(function(){
				$.lastSelectChanged = this;
			});
			$(lastSelectAdded).change();
		});
	}
}

function sliceListChange(user) {
	
	var totSlice = $('#edit-slice-list').find('div.row').length;
		
	if (totSlice <= 1) {
	
		var btn = $('#edit-slice-list').find('span.glyphicon-minus').parent('button.btn');
		btn[0].disabled = true;
	}
	
	help();
}

function removableSliceName(user, sliceName, inputclass, readonly, hideLabels) {
	readonly = readonly || false;
	var sliceNamesList = [];
	
	var sliceList = getCachedSliceList();
	
	$(sliceList).each(function() {
		var sl = this.name;
		
		sliceNamesList.push({
			'value' : this.name,
			'label' : sl
		});
	});
	sliceName = sliceName || "";

	var html = '';
	html += '<div class="row ' + inputclass + '">';
	html += '<div class="col-xs-6">';
	
	html += '<label>Name</label>';
	html += dynamicSelect('name', sliceName, sliceNamesList);
	html += '</div>';

	html += '<div class="col-xs-2">';
	html += '<label>&nbsp;</label>';
	
	$.serv = user;
	html += '<div><button onclick="javascript: \
						if($(\'#edit-slice-list\').find(\'span.glyphicon-minus\').length > 1)\
						{$(this).parent().parent().parent().remove();sliceListChange($.serv);}"\
			 class="btn btn-default btn-flat btn-sm pull-right" type="button"><span class="glyphicon glyphicon-minus"></span></button></div>';
	html += '</div>';
	html += '</div>';
	return html;
}

function showUserList(data, targetDom) {
	var users = data.data.users;
	var content = '';
	var toUpdate = false;
	
	content += '<div data-help-label="Username" data-help-key="username" />';
	content += '<div data-help-label="Role" data-help-key="role" />';
	content += '<div data-help-label="Org" data-help-key="org" />';
	content += '<div data-help-label="Actions" data-help-key="actions" />';
	content += '<div class="table-responsive">';
	content += '<table class="table table-hover table-striped"><thead><tr>';
	content += '<th>Username</th>';
	content += '<th>Role</th>';
	content += '<th>Org</th>';
	content += '<th>Actions</th>';
	content += '<th></th></tr></thead><tboby>';

	$(users)
			.each(
					function() {
						if (this.username !== getLoggedUserName()) {
							content += '<tr><td>' + this.username +'</td>';
							content += '<td>' + this.role + '</td>';
							content += '<td>' + this.org + '</td>';
							content += '</td>';

							if (this.role != 'USER') {
								toUpdate = true;
							} else {
								toUpdate = false;
							}
								
							content += '<td><div class="inline-toolbar" data-target-name="' + this.username
									+  '" data-target-object="user"' + '" data-target-update="' + toUpdate +'" ></div></td></tr>';
							
						}

					});
	content += '</tbody><tfoot></tfoot></table>';
	content += '</div>';

	$(targetDom).html(content);

	$(targetDom).find('.change-role').each(
			function() {
				var changeForm = $(this);
				var currentRole = changeForm.data('currentRole');
				var applyButton = changeForm.find('button');
				var roleSelector = changeForm.find('select');
				applyButton.attr('disabled', 'disabled');
				roleSelector.change(function() {
					if (currentRole != $(this).val()) {
						applyButton.removeAttr('disabled');
					} else {
						applyButton.attr('disabled', 'disabled');
					}
				});
				applyButton.click(function() {
					WS_updateUser(buildInput(changeForm.parent()), function(data) {
						$('.display-message').each(
								function() {
									$(this).html(
											'<div class="alert alert-success">Changed privileges for user '
													+ data.data.username + '</div>');
								});
						WS_getUserList(showUserList, targetDom);
					}, targetDom);
				});
			});

	$(targetDom).find('.change-user-password').each(
			function() {
				var changeForm = $(this);
				var changeButton = changeForm.find('.btn-change');
				var cancelButton = changeForm.find('.btn-cancel');
				var applyButton = changeForm.find('.btn-apply');
				var pwdInput = changeForm.find('input[type=password]');
				pwdInput.hide();
				cancelButton.hide();
				applyButton.hide();
				changeButton.show();
				changeButton.click(function() {
					cancelButton.show();
					applyButton.show();
					changeButton.hide();
					pwdInput.show();
				});
				cancelButton.click(function() {
					pwdInput.hide();
					cancelButton.hide();
					applyButton.hide();
					changeButton.show();
				});
				applyButton.click(function() {
					WS_updateUser(buildInput(changeForm.parent()), function(data) {
						$('.display-message').each(
								function() {
									$(this).html(
											'<div class="alert alert-success">Password succesfully changed for user '
													+ data.data.username + '</div>');
								});
						WS_getUserList(showUserList, targetDom);
					}, targetDom);
				});
			});

	inlineUserUpdateButton('.inline-toolbar');
	
	//inlineUserUpdateButton('.inline-toolbar', targetDom, displayAssociationUserSlicesPage(name, targetDom));

	inlineConfirmDeleteButton('.inline-toolbar', targetDom, function(name, targetDom) {
		WS_deleteUser(name, function(data, targetDom) {
			$('.display-message').each(function() {
				$(this).html('<div class="alert alert-success">' + data.message + '</div>');
			});
			WS_getUserList(showUserList, targetDom);
		}, targetDom);
	});
	
	help();
}

function passwordChangeForm() {
	$('#form-change-password').hide();
	$('#form-change-password *').click(function(e) {
		e.preventDefault();
		e.stopPropagation();
	});
	$('#change-password').click(function(e) {
		e.preventDefault();
		e.stopPropagation();
		$('#form-change-password').show();
		$('#user-icon').hide();
	});
	$('#form-change-password .btn-cancel').click(function(e) {
		e.preventDefault();
		e.stopPropagation();
		$('#form-change-password').hide();
		$('#user-icon').show();
	});
	$('#form-change-password .btn-save').click(
			function() {
				if ($('#newPassword').val() === $('#retypeNewPassword').val()) {
					WS_changePwd(buildInput($('#form-change-password')), function(data) {
						$('.display-message').each(
								function() {
									$('#form-change-password').hide();
									$('#user-icon').show();
									$(this).html(
											'<div class="alert alert-success">' + 'Password successfully changed.'
													+ 'Will be effective from next login' + '</div>');
								});
					});
				} else {
					$('.display-message')
							.each(
									function() {
										$(this).html(
												'<div class="alert alert-danger">'
														+ 'New password and re-typed new password are not the same'
														+ '</div>');
									});
				}
			});
}

$('#user-icon small').text(getRoleName());
passwordChangeForm();
addVisualSecurity();


/* User Management */
function WS_getUser(username, handler, targetDom) {
	getValues(_BASE_WEB_ROOT + _UM_SERVICES + '/' + username, handler, null, targetDom);
}
function WS_getUserList(handler, targetDom) {
	console.log("WS_getUserList");
	getValues(_BASE_WEB_ROOT + _UM_SERVICES, handler, null,targetDom);
}
function WS_createUser(inputData, handler, targetDom) {
	console.log("WS_createUser : ",inputData);
	putValues(_BASE_WEB_ROOT + _UM_SERVICES + '/', inputData, handler, null,targetDom, 'modal-body');
}
function WS_deleteUser(username, handler, targetDom) {
	deleteValues(_BASE_WEB_ROOT + _UM_SERVICES + '/' + username, handler, null,targetDom, 'modal-body');
}
function WS_updateUser(inputData, handler, targetDom) {
	console.log("WS_updateUserinputData : ",inputData);
	postValues(_BASE_WEB_ROOT + _UM_SERVICES + '/', inputData, handler, null,targetDom, 'modal-body');
}
function WS_getUserTemplate(handler, targetDom) {
	getValues(_BASE_WEB_ROOT + _UM_SERVICES + '/template', handler, null,targetDom);
}
function WS_changePwd(inputData, handler, targetDom) {
	postValues(_BASE_WEB_ROOT + _UM_SERVICES + '/changePwd', inputData, handler, null,targetDom);
}

function getCachedSliceList() {
	console.log("getCachedSliceList ");
	var cacheSlice = Preloader.getResource('slice').slices;
//	{"code":200,"message":"Success","data":{"slices":[{"name":"Monitoring","nodes":[{"id":1,"name":"Monitoring"}]}]}}
	console.log("getCachedSliceList cacheSlice : ",cacheSlice);
	return $(cacheSlice);
}

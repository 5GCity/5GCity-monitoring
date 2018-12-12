var _UM_SERVICES = '/um';
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
} , {
	'value' : 'MASSPROVISIONING',
	'label' : 'Massive Provisioning',
	'type' : ''
} ];

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

function isMassProv() {
	return _LOGGED_USER_ROLE === "MASSPROVISIONING";
}


function addVisualSecurity() {
	if (isUser() || isMassProv()) {
		$('.user-deny-parent').parent().remove();
		$('.user-deny').remove();
	}
	if (isAdmin()) {
		$('.admin-deny').remove();
	}
}

function help(){

	$('div#DataTables_Table_0_wrapper select[name="DataTables_Table_0_length"]').addClass("data-help-skip");
	$.Help.write([ {
		"name" : "User",
		"localHelp" : true
	} ]);
}

function showUserList(data, targetDom) {
	var users = data.data.users;
	var content = '';

	content += '<div data-help-label="Username" data-help-key="username" />';
	content += '<div data-help-label="Role" data-help-key="role" />';
	content += '<div data-help-label="Actions" data-help-key="actions" />';
	content += '<div class="table-responsive">';
	content += '<table class="table table-hover table-striped"><thead><tr>';
	content += '<th>Username</th>';
	content += '<th>Role</th>';
	content += '<th>Actions</th>';
	content += '<th></th></tr></thead><tboby>';

	$(users)
			.each(
					function() {
						if (this.username !== getLoggedUserName()) {
							content += '<tr><td>' + this.username + '</td>';
							content += '<td style="min-width:250px;">';
							content += '<div class="input-group input-group-sm data-input-l1 change-role" data-current-role="'
									+ this.role + '">';
							content += dynamicSelect('role', this.role, userRoles);
							content += '<input class="data-input" type="hidden" name="username" value="'
									+ this.username + '">';
							content += '<span class="input-group-btn">';
							content += '<button class="btn btn-default btn-flat" type="button">Change</button>';
							content += '</span>';
							content += '</div>';
							content += '</td>';
							content += '<td>';
							content += '<div class="input-group input-group-sm data-input-l1 change-user-password">';
							content += '<input class="data-input form-control data-help-skip" type="password" name="passwd" placeholder="Password">';
							content += '<input class="data-input form-control" type="hidden" name="username" value="'
									+ this.username + '">';
							content += '<span class="input-group-btn">';
							content += '<button class="btn btn-default btn-flat btn-change" type="button">Reset password</button>';
							content += '</span>';
							content += '<span class="input-group-btn">';
							content += '<button class="btn btn-default btn-flat btn-cancel" type="button">Cancel</button>';
							content += '</span>';
							content += '<span class="input-group-btn">';
							content += '<button class="btn btn-danger btn-flat btn-apply" type="button">Apply</button>';
							content += '</span>';
							content += '</div>';
							content += '</td>';
							content += '<td><div class="inline-toolbar" data-target-name="' + this.username
									+ '"></div></td></tr>';
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
	getValues(_BASE_WEB_ROOT + _UM_SERVICES + '/' + username, handler, null,targetDom);
}
function WS_getUserList(handler, targetDom) {
	getValues(_BASE_WEB_ROOT + _UM_SERVICES, handler, null,targetDom);
}
function WS_createUser(inputData, handler, targetDom) {
	putValues(_BASE_WEB_ROOT + _UM_SERVICES + '/', inputData, handler, null,targetDom, 'modal-body');
}
function WS_deleteUser(username, handler, targetDom) {
	deleteValues(_BASE_WEB_ROOT + _UM_SERVICES + '/' + username, handler, null,targetDom, 'modal-body');
}
function WS_updateUser(inputData, handler, targetDom) {
	postValues(_BASE_WEB_ROOT + _UM_SERVICES + '/', inputData, handler, null,targetDom, 'modal-body');
}
function WS_getUserTemplate(handler, targetDom) {
	getValues(_BASE_WEB_ROOT + _UM_SERVICES + '/template', handler, null,targetDom);
}
function WS_changePwd(inputData, handler, targetDom) {
	postValues(_BASE_WEB_ROOT + _UM_SERVICES + '/changePwd', inputData, handler, null,targetDom);
}

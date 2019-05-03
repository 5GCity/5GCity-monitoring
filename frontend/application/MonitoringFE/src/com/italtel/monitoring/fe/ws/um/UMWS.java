package com.italtel.monitoring.fe.ws.um;

import java.security.Principal;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.italtel.monitoring.fe.common.service.UserManagementService;
import com.italtel.monitoring.fe.entity.um.User;
import com.italtel.monitoring.fe.ws.BaseWS;
import com.italtel.monitoring.fe.ws.entity.Result;
import com.italtel.monitoring.fe.ws.log.LoggableWS;
import com.italtel.monitoring.fe.ws.um.entity.PasswordInfo;
import com.italtel.monitoring.fe.ws.um.entity.Users;


@Stateless
//@SecurityDomain("omDomain")
//@RolesAllowed({ "SYSTEM_ADMIN" })
@WebService(endpointInterface = "com.italtel.monitoring.fe.ws.um.UMWSInterface")
public class UMWS extends BaseWS implements UMWSInterface {

	private final static Logger log = LoggerFactory.getLogger(UMWS.class);
	@EJB(
			lookup = "java:global/FRONTEND/MonitoringFE/UserManagementBean!com.italtel.monitoring.fe.common.service.UserManagementService")

	private UserManagementService userManagementService;

	/*******************
	 * User Management *
	 *******************/

	@Override
	@LoggableWS
	public Result<User> createUser(User user) {
		Result<User> result;

		try {
			// Write
			User u = userManagementService.createUser(user);
			log.info("User created: {}", u);

			// Success result
			result = new Result<User>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(u);
		} catch (Exception e) {
			log.error("Error creating {}: {}", user, e.getMessage());

			// Error result
			result = new Result<User>(e);
		}

		return result;
	}

	@Override
//	@PermitAll
	@LoggableWS
	public Result<?> changePassword(PasswordInfo passwordInfo) {
		Result<?> result;

		try {
			// Change password
			Principal principal = getPrincipal();
			userManagementService.changePassword(principal.getName(),
					passwordInfo.getOldPassword(),
					passwordInfo.getNewPassword());
			log.info("User password changed (username={})", principal.getName());

			// Success result
			result = new Result<Object>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
		} catch (Exception e) {
			log.error("Error changing password of user with username={}: {}",
					getPrincipal().getName(), e.getMessage());

			// Error result
			result = new Result<Object>(e);
		}

		return result;
	}

	@Override
	@LoggableWS
	public Result<User> updateUser(User user) {
		Result<User> result;
		log.debug("User updated user :"+user);
		try {
			if (getPrincipal().getName().equals(user.getUsername())) {
				throw new IllegalAccessException(
						"You cannot change your user (if you want change your password invoke change password web service)");
			}

			// Write
			User u = userManagementService.updateUser(user);
			log.debug("User updated: {}", u);
			// Success result
			result = new Result<User>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(u);
		} catch (Exception e) {
			log.error("Error updating {}: {}", user, e.getMessage());

			// Error result
			result = new Result<User>(e);
		}

		return result;
	}

	@Override
	@LoggableWS
	public Result<?> deleteUser(String username) {
		Result<?> result;

		try {
			if (getPrincipal().getName().equals(username)) {
				throw new IllegalAccessException("You cannot delete your user");
			}

			// Delete
			userManagementService.deleteUser(username);
			log.info("User deleted (username={})", username);

			// Success result
			result = new Result<Object>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
		} catch (Exception e) {
			log.error("Error deleting user with username={}: {}", username,
					e.getMessage());

			// Error result
			result = new Result<Object>(e);
		}

		return result;
	}

	@Override
	@LoggableWS
	public Result<User> getUser(String username) {
		Result<User> result;

		try {
			// Read
			User u = userManagementService.getUser(username);
			log.info("Read user: {}", u);

			// Success result
			result = new Result<User>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(u);
		} catch (Exception e) {
			log.error("Error reading user with username={}: {}", username,
					e.getMessage());

			// Error result
			result = new Result<User>(e);
		}

		return result;
	}

	@Override
	@LoggableWS
	public Result<Users> listUsers() {
		try {
			// Read
			List<User> us = userManagementService.getUsers();
			log.info("Read user list (size {})", us.size());

			// Success result
			return new Result<Users>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE, new Users(us));
		} catch (Exception e) {
			log.error("Error reading user list: {}", e.getMessage());

			// Error result
			return new Result<Users>(e);
		}
	}

	@Override
	@LoggableWS
	public Result<User> getDefaultUser() {
		try {
			// Success result
			return new Result<User>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE,
					userManagementService.getDefaultUser());
		} catch (Exception e) {
			log.error("Error getting default user: {}", e.getMessage());

			// Error result
			return new Result<User>(e);
		}

	}

}
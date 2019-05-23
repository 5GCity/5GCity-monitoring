package com.italtel.monitoring.fe.common.service.impl;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.italtel.monitoring.fe.entity.Slice;
import com.italtel.monitoring.fe.entity.um.RoleType;
import com.italtel.monitoring.fe.entity.um.User;
import com.italtel.monitoring.fe.entity.um.UserSlice;
import com.italtel.monitoring.fe.utils.DBUtility;
import com.italtel.monitoring.fe.common.service.UserManagementService;
import com.italtel.monitoring.fe.common.service.exception.AlreadyExistsNameConfException;
import com.italtel.monitoring.fe.common.service.exception.ConfigException;
import com.italtel.monitoring.fe.common.service.exception.InvalidDataConfException;
import com.italtel.monitoring.fe.common.service.exception.NotFoundNameConfException;

@Singleton
@Startup
@Local({ UserManagementService.class })
//@DependsOn("ApplicationControlBean")
public class UserManagementBean implements UserManagementService {

	private final static Logger log = LoggerFactory
			.getLogger(UserManagementBean.class);

	private final static String DEFAULT_SYSTEM_ADMIN_USERNAME = "Italtel";
	private final static String DEFAULT_SYS_PASSWORD = "Italtel123";
	private final static String DEFAULT_ADMIN_USERNAME = "Admin";
	private final static String DEFAULT_PASSWORD = "Admin";
//	private static final String SYSORGMON = "SYSMONITORING";
//	private static final String ORGMON = "MONITORING";
	
	private final static RoleType DEFAULT_ROLE = RoleType.USER;

	private final static String DEFAULT_SYSTEM_ADMIN_PASSWORD = DEFAULT_SYS_PASSWORD;
	private final static String DEFAULT_ADMIN_PASSWORD = DEFAULT_PASSWORD;
	
	@PersistenceContext(unitName = "MONITORING-DB")
	EntityManager em;

	@PostConstruct
	public void init() {
		log.info("%%% UserManagementBean Initialization Started %%%");
		if (countUserWithRole(RoleType.SYSTEM_ADMIN) == 0) {
			try {
				User u = new User();
				u.setUsername(DEFAULT_SYSTEM_ADMIN_USERNAME);
				u.setPasswd(DEFAULT_SYSTEM_ADMIN_PASSWORD);
				u.setRole(RoleType.SYSTEM_ADMIN);
				u.setOrg(User.DEF_SYSTEM_ORG);
				createUser(u);
			} catch (Exception e) {
				throw new RuntimeException(
						"Cannot create default SYSTEM_ADMIN user: "
								+ e.getMessage(), e);
			}
		}
		if (countUserWithRole(RoleType.ADMIN) == 0) {
			try {
				User u = new User();
				u.setUsername(DEFAULT_ADMIN_USERNAME);
				u.setPasswd(DEFAULT_ADMIN_PASSWORD);
				u.setRole(RoleType.ADMIN);	
				u.setOrg(User.DEF_ADMIN_ORG);
				createUser(u);
			} catch (Exception e) {
				throw new RuntimeException(
						"Cannot create default SYSTEM_ADMIN user: "
								+ e.getMessage(), e);
			}
		}
		log.info("%%% UserManagementBean Initialization Finished %%%");
	}

	@PreDestroy
	public void destroy() {
		log.info("%%% UserManagementBean Clean Started %%%");
		log.info("%%% UserManagementBean Clean Finished %%%");
	}

	@Override
	public void changePassword(String username, String oldPassword,
			String newPassword) throws ConfigException {
		User user = getUser(username);

		if (oldPassword == null
				|| !user.getPasswd().equals(
						getBase64HashedFromPassword(oldPassword))) {
			throw new InvalidDataConfException("Incorrect old password");
		}

		user.setPasswd(getBase64HashedFromPassword(newPassword));

		log.debug("Changed password of user: {}", user);
	}

	@Override
	public User createUser(User user) throws ConfigException {
		String username = user.getUsername();
		if (username == null || username.length() == 0) {
			throw new InvalidDataConfException(
					"Invalid username. It must be not empty!");
		}

		if (getInternalUser(username) != null) {
			throw new AlreadyExistsNameConfException(username,
					"Already exists an user with username '"
							+ user.getUsername() + "'");
		}

		user.setPasswd(getBase64HashedFromPassword(user.getPasswd()));

		if (user.getRole() == null) {
			user.setRole(DEFAULT_ROLE);
		}

		try {
			em.persist(user);
			log.debug("Create new user: {}", user);
		} catch (Exception e) {
			String errMsg = "Error creating a new user with username '"
					+ user.getUsername() + "': " + e.getMessage();
			log.error(errMsg, e);
			throw new ConfigException(errMsg);
		}
		return user;
	}

	public void manageSlicesInUser(User user)
			throws ConfigException {
		List<UserSlice> userSlices = user.getUserSlices();

		log.debug("manageSlicesInUser userSlices : "+userSlices);
		
		Set<String> sliceNames = new HashSet<>();		

		if ((userSlices != null) && (!userSlices.isEmpty())) {
			validateSliceInUser(userSlices, sliceNames);
		} else {
			log.error("At least one slice must be present");
			throw new ConfigException("At least one slice must be present");
		}
	}

	public void validateSliceInUser(
			List<UserSlice> userSlices,
			Set<String> sliceNames) throws ConfigException {
		for (UserSlice us : userSlices) {
			String sliceName = us.getSliceName();
			log.debug("validateSliceInUser sliceName : "+sliceName);
			if (sliceName == null || sliceName.isEmpty()) {
				throw new ConfigException("Slice name is null or empty");
			}

			Map<String, Object> queryParams2 = new HashMap<String, Object>();
			queryParams2.put("name", sliceName);

			Slice result = DBUtility
					.getNamedQuerySingleResultByNamedParams(em,
							Slice.QUERY_READ_SLICE, Slice.class,
							queryParams2);

			if (result == null) {
				log.error("Slice not found, name:{}", sliceName);
				throw new ConfigException("Slice not found");
			} else {
				log.debug("validateSliceInUser result : "+result.toString());
				us.setSlice(result);
			}

			if (!sliceNames.add(sliceName)) {
				throw new ConfigException(
						"duplicated slice with name '" + sliceName + "'");
			}
		}
	}

	@Override
	public User updateUser(User user) throws ConfigException {
		User currUser = getUser(user.getUsername());
		log.debug("Updated user: {}", currUser);
		
		List<UserSlice> userSlices = user.getUserSlices();
		
		log.debug("Updated userSlices : "+userSlices);
		
		if (user.getPasswd() != null && !user.getPasswd().isEmpty()) {
			currUser.setPasswd(getBase64HashedFromPassword(user.getPasswd()));
		}

		if (user.getRole() != null) {
			currUser.setRole(user.getRole());
		}

		manageSlicesInUser(user);
		
		currUser.setUserSlices(user.getUserSlices());
		
		return currUser;
	}

	@Override
	public void deleteUser(String username) throws ConfigException {
		User user = getUser(username);

		try {
			em.remove(user);

			log.debug("Removed user: {}", user);
		} catch (Exception e) {
			String errMsg = "Error deleting user with username '" + username
					+ "': " + e.getMessage();
			log.error(errMsg, e);
			throw new ConfigException(errMsg);
		}
	}

	@Override
	public User getUser(String username) throws NotFoundNameConfException {
		User user = getInternalUser(username);
		if (user == null) {
			throw new NotFoundNameConfException(username,
					"Not found an user with username '" + username + "'");
		}
		return user;
	}

	@Override
	public List<User> getUsers() {
		List<User> users = DBUtility.getNamedQueryResultList(em,
				User.QUERY_READ_ALL_USERS, User.class);
		log.debug("Found {} users", users.size());
		return users;
	}

	@Override
	public User getDefaultUser() {
		List<UserSlice> uslice = new ArrayList<UserSlice>();
		return new User("", "", RoleType.USER,"",uslice);
	}

	private User getInternalUser(String username) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("username", username);
		User user = DBUtility.getNamedQuerySingleResultByNamedParams(em,
				User.QUERY_READ_USER_BY_USERNAME, User.class, queryParams);

		if (user != null) {
			log.debug("Found user: {}", user);
		} else {
			log.debug("Not found user with username={}", username);
		}
		return user;
	}

	private Long countUserWithRole(RoleType role) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("role", role);
		return DBUtility.getNamedQuerySingleResultByNamedParams(em,
				User.QUERY_COUNT_USERS_WITH_SELECTED_ROLE, Long.class,
				queryParams);
	}

	private String getBase64HashedFromPassword(String password)
			throws ConfigException {
		if (password == null || password.isEmpty()) {
			throw new InvalidDataConfException(
					"Invalid password. It must be not empty!");
		}

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] passwordBytes = password.getBytes();
			byte[] hash = md.digest(passwordBytes);
			return Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			throw new ConfigException("Error hashing password: "
					+ e.getMessage(), e);
		}
	}
}

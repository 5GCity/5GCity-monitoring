package com.italtel.monitoring.fe.common.service;

import java.util.List;

import com.italtel.monitoring.fe.entity.um.User;
import com.italtel.monitoring.fe.common.service.exception.ConfigException;
import com.italtel.monitoring.fe.common.service.exception.NotFoundNameConfException;

public interface UserManagementService {

	public void changePassword(String username, String oldPassword,
			String newPassword) throws ConfigException;

	public User createUser(User user) throws ConfigException;
	
	public User updateUser(User user) throws ConfigException;

	public void deleteUser(String username) throws ConfigException;

	public List<User> getUsers();

	public User getUser(String username) throws NotFoundNameConfException;

	public User getDefaultUser();
}
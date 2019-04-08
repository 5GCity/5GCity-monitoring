package com.italtel.monitoring.fe.security;

import java.util.Collections;
import java.util.List;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.SimplePrincipal;

public class CustomPrincipal extends SimplePrincipal {

	private static final long serialVersionUID = -6507574870617090370L;

	private List<String> roles;

	public CustomPrincipal(String name) {
		super(name);
	}

	public void setRoles(List<String> roles) {
		if (this.roles != null) {
			PicketBoxLogger.LOGGER.warn("Roles are already setted to " + roles);
			return;
		}
		this.roles = roles;
	}

	public List<String> getRoles() {
		if (roles == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(roles);
	}

	public boolean hasRole(String role) {
		if (roles == null) {
			return false;
		}
		return roles.contains(role);
	}

}

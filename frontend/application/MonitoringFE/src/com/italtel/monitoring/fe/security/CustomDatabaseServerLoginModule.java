package com.italtel.monitoring.fe.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.jboss.security.auth.spi.DatabaseServerLoginModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.italtel.monitoring.fe.utils.MDCHelper;
import com.italtel.monitoring.fe.xss.XSSPreventionFilter;


public class CustomDatabaseServerLoginModule extends DatabaseServerLoginModule {

	private final static Logger log = LoggerFactory
			.getLogger(CustomDatabaseServerLoginModule.class);
	private final static Logger activityLog = LoggerFactory
			.getLogger("ACTIVITY_LOG");

	@Override
	protected Principal createIdentity(String username) throws Exception {
		if (!XSSPreventionFilter.checkXSS(username)) {
			return null;
		}
		return new CustomPrincipal(username);
	}

	@Override
	public boolean commit() throws LoginException {
		boolean commit = super.commit();

		if (commit) {
			Principal identity = getIdentity();

			if (identity != null && identity instanceof CustomPrincipal) {
				Group[] roleSets = getRoleSets();
				for (Group group : roleSets) {
					if (group.getName().equals("Roles")) {
						List<String> roles = new ArrayList<String>();
						Enumeration<? extends Principal> en = group.members();
						while (en.hasMoreElements()) {
							roles.add(en.nextElement().getName());
						}

						((CustomPrincipal) identity).setRoles(roles);
						log.debug("Set roles {} on principal {}", roles,
								identity);

						if (useFirstPass) {
							Principal sharedPrincipal = (CustomPrincipal) sharedState
									.get("javax.security.auth.login.name");
							if (sharedPrincipal != null
									&& sharedPrincipal instanceof CustomPrincipal) {
								((CustomPrincipal) sharedPrincipal)
										.setRoles(roles);
								log.debug(
										"Set roles {} on shared principal {}",
										roles, sharedPrincipal);
							}
						}

						break;
					}
				}
			}
		}

		return commit;
	}

	@Override
	public boolean login() throws LoginException {
		MDCHelper.startTx();
		try {
			log.debug("Login ");
			boolean login = super.login();
			if (!login) {
				raiseAuthFailure();
			}
			logLogin(login);
			return login;
		} catch (Exception e) {
			raiseAuthFailure();
			logLogin(false);
			throw e;
		} finally {
			MDCHelper.endTx();
		}
	}

	private void raiseAuthFailure() {
		String username = getUsername();
		if (username != null) {
			AuthenticationFailurePublisher authFailurePub = new AuthenticationFailurePublisher();
			try {
				authFailurePub.start();
			} catch (Exception e) {
				log.error("Error starting {}: ", authFailurePub,
						e.getMessage(), e);
				return;
			}

			try {
				authFailurePub.raiseAuthFailure(username);
				log.debug("Sent authentication failure event for username {}",
						username);
			} catch (Exception e) {
				log.error(
						"Error sending authentication failure event for username {}: {}",
						username, e.getMessage(), e);
			} finally {
				authFailurePub.stop();
			}
		}
	}

	private void logLogin(boolean login) {
		String username = getUsername();
		if (username != null) {
			activityLog.info("User {} {}", username, login ? "logged in"
					: "failed authentication");
		}
	}
}

package com.italtel.monitoring.fe.webgui.listener;

import java.security.Principal;

import javax.annotation.Resource;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.security.CacheableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class SessionListener implements HttpSessionListener {

	private final static Logger log = LoggerFactory
			.getLogger(SessionListener.class);

	public static final String SESSION_PRINCIPAL_ATTRIBUTE = "principal";

	@Resource(name = "java:jboss/jaas/omDomain/authenticationMgr")
	private CacheableManager<?, Principal> authenticationManager;

	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		log.trace("Created session={}", httpSessionEvent.getSession().getId());
		HttpSession session = httpSessionEvent.getSession();
		session.setAttribute("sidebarCollapsed", Boolean.FALSE);
		session.setAttribute("collapseClass", "");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		log.trace("Destroyed session={}", httpSessionEvent.getSession().getId());

		Principal principal = (Principal) httpSessionEvent.getSession()
				.getAttribute(SESSION_PRINCIPAL_ATTRIBUTE);
		if (principal != null && authenticationManager != null) {
			authenticationManager.flushCache(principal);
			log.trace("Flush principal={} from cache", principal);
		}
	}
}
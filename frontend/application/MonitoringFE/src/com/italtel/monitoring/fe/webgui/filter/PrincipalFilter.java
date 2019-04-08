package com.italtel.monitoring.fe.webgui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.italtel.monitoring.fe.webgui.listener.SessionListener;


//@WebFilter(filterName = "PrincipalFilter",
//		description = "This filter set on session the request user principal",
//		urlPatterns = { "*.jsp", "*.html" })
public class PrincipalFilter implements Filter {

	private final static Logger log = LoggerFactory
			.getLogger(PrincipalFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		
		log.debug("Entering PrincipalFilter");
		
		if (servletRequest instanceof HttpServletRequest) {
			
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			log.debug("Entering PrincipalFilter request :"+request.toString());
			boolean authenticated = request.getUserPrincipal() != null;
			log.debug("Entering PrincipalFilter authenticated :"+authenticated);
			HttpSession session = request.getSession(false);
			if(session != null)
				log.debug("Entering PrincipalFilter session :"+session.toString());
			if (authenticated
					&& session != null
					&& session
							.getAttribute(SessionListener.SESSION_PRINCIPAL_ATTRIBUTE) == null) {
				log.debug("Authenticated principal={} on session={}",
						request.getUserPrincipal(), session.getId());

				request.changeSessionId();
				log.debug(
						"Prevent session fixation attack changing session ID to {}",
						session.getId());

				log.debug("Set princial={} on session={} ",
						request.getUserPrincipal(), session.getId());
				session.setAttribute(
						SessionListener.SESSION_PRINCIPAL_ATTRIBUTE,
						request.getUserPrincipal());
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}
}

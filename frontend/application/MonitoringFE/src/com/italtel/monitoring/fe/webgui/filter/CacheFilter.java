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
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@WebFilter(filterName = "CacheFilter",
//		description = "This filter set cache header", urlPatterns = { "*.jsp",
//				"*.html" })
public class CacheFilter implements Filter {

	private final static Logger log = LoggerFactory
			.getLogger(CacheFilter.class);

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
		
		log.debug("Entering CacheFilter");		
		
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

			httpServletResponse.setHeader("Cache-Control",
					"no-cache, no-store, must-revalidate");
			httpServletResponse.setHeader("Pragma", "no-cache");
			httpServletResponse.setHeader("Expires", "0");

			log.debug("REQUEST: {}", httpServletRequest.getRequestURI());
			if (httpServletRequest.getRequestURI().contains("/login.jsp")) {
				log.debug("login.jsp access detected, redirecting to /");
				httpServletResponse.sendRedirect(httpServletRequest
						.getRequestURI().replaceFirst("/login\\.jsp.*", "/"));
				return;
			}
		} catch (Exception e) {
			log.error("ERROR!", e);
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}
}

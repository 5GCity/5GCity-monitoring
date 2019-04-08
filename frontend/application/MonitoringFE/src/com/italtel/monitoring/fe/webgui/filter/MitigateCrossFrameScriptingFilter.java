package com.italtel.monitoring.fe.webgui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@WebFilter(
//		filterName = "MitigateCrossFrameScriptingFilter",
//		description = "This filter prevent cross-frame scripting attacks adding X-Frame-Options header setted to DENY",
//		urlPatterns = { "*.jsp", "*.html" })
public class MitigateCrossFrameScriptingFilter implements Filter {
	
	private final static Logger log = LoggerFactory
			.getLogger(MitigateCrossFrameScriptingFilter.class);	

	private static final String X_FRAME_OPTIONS_HEADER = "X-Frame-Options";
	private static final String X_FRAME_OPTIONS_HEADER_VALUE_DENY = "DENY";

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		log.debug("Entering MitigateCrossFrameScriptingFilter");		

		if (response instanceof HttpServletResponse) {
			HttpServletResponse res = (HttpServletResponse) response;
			if (res.containsHeader(X_FRAME_OPTIONS_HEADER)) {
				res.setHeader(X_FRAME_OPTIONS_HEADER,
						X_FRAME_OPTIONS_HEADER_VALUE_DENY);
				log.debug("X-Frame Option Header set");
			} else {
				res.addHeader(X_FRAME_OPTIONS_HEADER,
						X_FRAME_OPTIONS_HEADER_VALUE_DENY);
				log.debug("X-Frame Option Header added");
			}
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}

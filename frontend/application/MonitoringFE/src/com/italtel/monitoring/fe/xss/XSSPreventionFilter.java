package com.italtel.monitoring.fe.xss;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.reference.JavaLogFactory;

public class XSSPreventionFilter implements Filter {

	{
		JavaLogFactory.getInstance().getLogger("org.owasp.esapi")
				.setLevel(org.owasp.esapi.Logger.OFF);
	}

	private static Pattern[] patterns = new Pattern[] {
			// Script fragments
			Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
			// src='...'
			Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL),
			// lonely script tags
			Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL),
			// eval(...)
			Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL),
			// expression(...)
			Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL),
			// javascript:...
			Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
			// vbscript:...
			Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
			// onload(...)=...
			Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL) };

	private static final Encoder encoder = ESAPI.encoder();

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		for (Entry<String, String[]> entry : httpRequest.getParameterMap()
				.entrySet()) {
			if (!checkXSS(entry.getKey())) {
				maliciousRequest(response);
				return;
			}
			for (String val : entry.getValue()) {
				if (!checkXSS(val)) {
					maliciousRequest(response);
					return;
				}
			}

		}

		Enumeration<String> headerNames = httpRequest.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			if (!checkXSS(httpRequest.getHeader(headerNames.nextElement()))) {
				maliciousRequest(response);
				return;
			}
		}

		if (!checkXSS(httpRequest.getRequestURI())) {
			maliciousRequest(response);
			return;
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				httpRequest.getInputStream(), StandardCharsets.UTF_8));
		final String body = br.lines().collect(
				Collectors.joining(System.lineSeparator()));
		br.close();

		if (!XSSPreventionFilter.checkXSS(body)) {
			maliciousRequest(response);
			return;
		}

		ServletInputStream inputStream = new ServletInputStream() {
			final ByteArrayInputStream byteArrayIS = new ByteArrayInputStream(
					body != null ? body.getBytes(StandardCharsets.UTF_8)
							: new byte[0]);

			@Override
			public int read() throws IOException {
				return byteArrayIS.read();
			}

			@Override
			public boolean isFinished() {
				return byteArrayIS.available() == 0;
			}

			@Override
			public boolean isReady() {
				return byteArrayIS.available() > 0;
			}

			@Override
			public void setReadListener(ReadListener arg0) {
			}
		};

		chain.doFilter(new XSSRequestWrapper(httpRequest, inputStream),
				response);

	}

	public void maliciousRequest(ServletResponse response) throws IOException {
		((HttpServletResponse) response).sendError(400);
	}

	public static boolean checkXSS(String value) {
		if (value != null) {
			try {
				String encodedValue = encoder.canonicalize(value);

				// Avoid null characters
				encodedValue = encodedValue.replaceAll("\0", "");

				// Remove all sections that match a pattern
				for (Pattern scriptPattern : patterns) {
					if (scriptPattern.matcher(encodedValue).find()) {
						return false;
					}
				}
			} catch (IntrusionException e) {
				return false;
			}
		}
		return true;
	}

}

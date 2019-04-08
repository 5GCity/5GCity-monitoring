package com.italtel.monitoring.fe.xss;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

	private ServletInputStream inputStream;

	public XSSRequestWrapper(HttpServletRequest servletRequest,
			ServletInputStream inputStream) {
		super(servletRequest);
		this.inputStream = inputStream;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return inputStream;
	}

}
package com.italtel.monitoring.fe.common.service.exception;

public class InvalidDataConfException extends ConfigException {

	private static final long serialVersionUID = 1L;

	public InvalidDataConfException(String message) {
		super(message);
	}

	public InvalidDataConfException(String message, Throwable cause) {
		super(message, cause);
	}

}

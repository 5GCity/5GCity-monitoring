package com.italtel.monitoring.fe.common.service.exception;

public class NotFoundNameConfException extends ConfigException {

	private static final long serialVersionUID = 1L;

	private String name;

	public NotFoundNameConfException(String name) {
		super(name);
		this.name = name;
	}

	public NotFoundNameConfException(String name, String message) {
		super(message);
		this.name = name;
	}

	public NotFoundNameConfException(String name, Throwable cause) {
		super(cause);
		this.name = name;
	}

	public NotFoundNameConfException(String name, String message,
			Throwable cause) {
		super(message, cause);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}

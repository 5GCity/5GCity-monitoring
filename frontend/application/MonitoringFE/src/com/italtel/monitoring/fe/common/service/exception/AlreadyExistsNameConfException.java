package com.italtel.monitoring.fe.common.service.exception;

public class AlreadyExistsNameConfException extends ConfigException {

	private static final long serialVersionUID = 1L;

	private String name;

	public AlreadyExistsNameConfException(String name) {
		super(name);
		this.name = name;
	}

	public AlreadyExistsNameConfException(String name, String message) {
		super(message);
		this.name = name;
	}

	public AlreadyExistsNameConfException(String name, Throwable cause) {
		super(cause);
		this.name = name;
	}

	public AlreadyExistsNameConfException(String name, String message,
			Throwable cause) {
		super(message, cause);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}

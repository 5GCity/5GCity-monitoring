package com.italtel.monitoring.fe.ws.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)
public class Result<T> {

	// Success code
	public static final int OK_SUCCESS_CODE = 200;
	// Client error code
	public static final int NOT_MODIFIED_ERROR_CODE = 304;
	public static final int BAD_REQUEST_ERROR_CODE = 400;
	public static final int UNAUTHORIZED_ERROR_CODE = 401;
	public static final int NOT_FOUND_ERROR_CODE = 404;
	public static final int TIMEOUT_ERROR_CODE = 408;
	// Server error code
	public static final int GENERIC_ERROR_CODE = 500;
	public static final int NOT_IMPLEMENTED_ERROR_CODE = 501;
	public static final int LICENSE_ERROR_CODE = 599;
	public static final int BUSY_EVERYWHERE_ERROR_CODE = 604;

	// Messages
	public static final String OK_SUCCESS_MESSAGE = "Success";
	public static final String NOT_IMPLEMENTED_ERROR_MESSAGE = "Not yet implemented";
	public static final String NOT_FOUND_MESSAGE = "RESOURCE NOT FOUND";
	public static final String CONSTRAINT_VIOLATION_MESSAGE = "CONSTRAINT VIOLATION";
	public static final String TIMEOUT_ON_WRITE_ACCESS_MESSAGE = "Timeout waiting another write operation is completed";
	public static final String INSUFFICENT_LICENSE = "Insufficent license";
	public static final String SERVER_BUSY_MESSAGE = "SERVER BUSY";
	public static final String GENERIC_ERROR_MESSAGE = "ERROR";

	// Patterns
	private static String KEY_VIOLATION_PATTERN = ".*\"PRIMARY.KEY.*ON PUBLIC\\.(.*)\\((.*)\\)\".*";

	@XmlElement(name = "code")
	private int code;

	@XmlElement(name = "message")
	private String message;

	@XmlElement(name = "data")
	private T data;

	public Result() {
	}

	public Result(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public Result(Exception cause) {
		int code = GENERIC_ERROR_CODE;
		StringBuilder message = new StringBuilder();
		if (cause instanceof IllegalArgumentException) {
			code = BAD_REQUEST_ERROR_CODE;
			message.append(GENERIC_ERROR_MESSAGE).append(": ")
					.append(cause.getMessage());
		} else if (cause instanceof IllegalAccessException) {
			code = UNAUTHORIZED_ERROR_CODE;
			message.append(GENERIC_ERROR_MESSAGE).append(": ")
					.append(cause.getMessage());
		} else {
			String originalMessage = cause.getMessage();
			Pattern p = Pattern.compile(KEY_VIOLATION_PATTERN, Pattern.DOTALL);
			// fix possible npe...
			Matcher m = p.matcher("" + originalMessage);
			if (m.matches()) {
				// code = BAD_REQUEST_ERROR_CODE;
				message.append(CONSTRAINT_VIOLATION_MESSAGE);
				if (m.groupCount() == 2) {
					message.append(": Duplicate ").append(m.group(1))
							.append(".").append(m.group(2));
				}
			} else {
				message.append(GENERIC_ERROR_MESSAGE).append(": ")
						.append(cause.getMessage());
			}
		}

		this.code = code;
		this.message = message.toString();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Result [code=");
		builder.append(code);
		builder.append(", message=");
		builder.append(message);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Result<?> other = (Result<?>) obj;
		if (code != other.code) {
			return false;
		}

		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}

		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else {
			if (other.data == null) {
				return false;
			}

			if (data.getClass().equals(other.data.getClass())) {
				return false;
			}

			if (!data.equals(other.data)) {
				return false;
			}
		}

		return true;
	}

}

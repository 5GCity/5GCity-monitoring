package com.italtel.monitoring.fe.security;

import java.util.HashMap;

import javax.jms.JMSException;

import com.italtel.monitoring.fe.event.message.BaseMessagePublisher;


public class AuthenticationFailurePublisher extends BaseMessagePublisher {

	private static final String FAULT_DEST_NAME = "java:/fault/topic";
	private static final String PUBLISHER_NAME = "AUTH_FAILURE_PUBLISHER";

	public static final String JMSMessageType = "AuthenticationFailureMessage";
	public static final String JMSMessagePropertyName_Username = "USERNAME";

	public AuthenticationFailurePublisher() {
		super(PUBLISHER_NAME, FAULT_DEST_NAME);
	}

	@Override
	protected String getDefaultJMSMessageType() {
		return JMSMessageType;
	}

	public void raiseAuthFailure(String username) throws JMSException {
		HashMap<String, Object> propMap = new HashMap<String, Object>();
		propMap.put(JMSMessagePropertyName_Username, username);

		sendMessage(null, propMap);
	}

}

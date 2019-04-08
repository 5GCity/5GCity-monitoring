package com.italtel.monitoring.fe.ws;

import java.security.Principal;

import javax.inject.Inject;

import com.italtel.monitoring.fe.ws.log.ILoggableWS;


public class BaseWS implements ILoggableWS {

	@Inject
	Principal principal;

	@Override
	public Principal getPrincipal() {
		return principal;
	}

}

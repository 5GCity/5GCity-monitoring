package com.italtel.monitoring.fe.utils.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PasswordAdapter extends XmlAdapter<String, String> {

	@Override
	public String marshal(String string) throws Exception {
		return null;
	}

	@Override
	public String unmarshal(String string) throws Exception {
		return string;
	}

}

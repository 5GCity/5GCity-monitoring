package com.italtel.monitoring.fe.entity;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AlertRules implements Serializable {

	private static final long serialVersionUID = -8372241727494839580L;
	
	@JsonProperty("alertrules")
	@XmlElementWrapper(name = "alertrules")
	@XmlElement(name = "alertrule")
	private List<AlertRule> alertrules;

	public AlertRules() {
	}

	public AlertRules(List<AlertRule> alertrules) {
		this.alertrules = alertrules;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AlertRules [alertrules=");
		builder.append(alertrules);
		builder.append("]");
		return builder.toString();
	}

}

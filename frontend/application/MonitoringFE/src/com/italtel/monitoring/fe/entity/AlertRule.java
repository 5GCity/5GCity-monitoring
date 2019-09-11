package com.italtel.monitoring.fe.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.italtel.monitoring.fe.common.service.OMEntity;

@Entity
@Table(schema = "MONITORING")
@NamedQueries({
		@NamedQuery(name = AlertRule.QUERY_READ_ALERTRULE,
				query = "select r from AlertRule r where r.name=:name"),
		@NamedQuery(name = AlertRule.QUERY_READ_ALL_ALERTRULES,
				query = "select r from AlertRule r") })
@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)
public class AlertRule implements OMEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String QUERY_READ_ALERTRULE = "findAlertRuleByName";
	public static final String QUERY_READ_ALL_ALERTRULES = "findAllAlertRules";
	
	@Id
	private String name;

	private String duration;
	private String severity;
	private String expression;
	private String summary;
	private String description;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AlertRule [name=");
		builder.append(name);
		builder.append(", duration=");
		builder.append(duration);
		builder.append(", severity=");
		builder.append(severity);
		builder.append(", expression=");
		builder.append(expression);
		builder.append(", summary=");
		builder.append(summary);
		builder.append(", description=");
		builder.append(description);
		builder.append("]");
		return builder.toString();
	}	

	
}

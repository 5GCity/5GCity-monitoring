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
public class NotificationMetrics implements Serializable {

	private static final long serialVersionUID = 3629514493712724392L;

	@JsonProperty("notificationMetrics")
	@XmlElementWrapper(name = "notificationMetrics")
	@XmlElement(name = "notificationMetric")
	private List<NotificationMetric> notificationMetrics;

	public NotificationMetrics() {
	}

	public NotificationMetrics(List<NotificationMetric> notificationMetrics) {
		this.notificationMetrics = notificationMetrics;
	}

	public List<NotificationMetric> getNotificationMetrics() {
		return notificationMetrics;
	}

	public void setNotificationMetrics(List<NotificationMetric> notificationMetrics) {
		this.notificationMetrics = notificationMetrics;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotificationMetrics [notificationMetrics=")
				.append(notificationMetrics).append("]");
		return builder.toString();
	}


	
}

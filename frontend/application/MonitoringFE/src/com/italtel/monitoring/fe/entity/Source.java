package com.italtel.monitoring.fe.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.italtel.monitoring.fe.common.service.OMEntity;


@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)
@Embeddable
public class Source implements OMEntity {

	private static final long serialVersionUID = -8372241727494839580L;
	public static final Integer DEF_SOURCE_PORT_MONITORING = 9100;
	public static final Integer DEF_SOURCE_PORT_GATEWAY = 10000;
	public static final Integer DEF_SOURCE_INTERVAL= 30;
	public static final Integer DEF_SOURCE_MAX_INTERVAL= 360;
	public static final Integer DEF_SOURCE_MAX_PORT= 65536;
	public static final String  DEF_SOURCE_METRICPATH = "/metrics";
	
	@XmlElement(required = false)
	@Column(nullable = true)
	private SourceTypeEnum sourceType;

	@XmlElement(required = false)
	@Column(nullable = true)
	private String metricPath;

	@XmlElement(required = false)
	@Column(nullable = true)
	private Integer interval;

	@XmlElement(required = false)
	@Column(nullable = true)
	private Integer port;
	
	@XmlElement(required = false)
	@Column(nullable = true)
	private String dashboardType;
	
	public SourceTypeEnum getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceTypeEnum sourceType) {
		this.sourceType = sourceType;
	}

	public String getMetricPath() {
		return metricPath;
	}

	public void setMetricPath(String metricPath) {
		this.metricPath = metricPath;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public String getDashboardType() {
		return dashboardType;
	}

	public void setDashboardType(String dashboardType) {
		this.dashboardType = dashboardType;
	}

	@Override
	public String toString() {
		return "Source [sourceType=" + sourceType + ", metricPath=" + metricPath + ", interval=" + interval + ", port="
				+ port + ", dashboardType=" + dashboardType + "]";
	}


}

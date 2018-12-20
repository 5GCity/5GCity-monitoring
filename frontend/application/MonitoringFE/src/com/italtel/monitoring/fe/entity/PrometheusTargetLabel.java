package com.italtel.monitoring.fe.entity;

public class PrometheusTargetLabel {

	private String metricGroup;
	private String instance;

	public String getMetricGroup() {
		return metricGroup;
	}

	public void setMetricGroup(String metricGroup) {
		this.metricGroup = metricGroup;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

}

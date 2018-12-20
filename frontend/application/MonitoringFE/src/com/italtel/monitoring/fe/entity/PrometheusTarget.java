package com.italtel.monitoring.fe.entity;

import java.util.List;

public class PrometheusTarget {

	private List<String> targets;
	private PrometheusTargetLabel labels;

	public PrometheusTargetLabel getLabels() {
		return labels;
	}

	public List<String> getTargets() {
		return targets;
	}

	public void setTargets(List<String> targets) {
		this.targets = targets;
	}

	public void setLabels(PrometheusTargetLabel labels) {
		this.labels = labels;
	}

}

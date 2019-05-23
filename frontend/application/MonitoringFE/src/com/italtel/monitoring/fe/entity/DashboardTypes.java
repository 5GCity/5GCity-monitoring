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
public class DashboardTypes implements Serializable {

	private static final long serialVersionUID = 3629514493712724392L;

	@JsonProperty("dashboardTypes")
	@XmlElementWrapper(name = "dashboardTypes")
	@XmlElement(name = "dashboardType")
	private List<DashboardType> dashboardTypes;

	public DashboardTypes() {
	}

	public List<DashboardType> getDashboardTypes() {
		return dashboardTypes;
	}

	public void setDashboardTypes(List<DashboardType> dashboardTypes) {
		this.dashboardTypes = dashboardTypes;
	}

	@Override
	public String toString() {
		return "DashboardTypes [dashboardTypes=" + dashboardTypes + "]";
	}

	

}

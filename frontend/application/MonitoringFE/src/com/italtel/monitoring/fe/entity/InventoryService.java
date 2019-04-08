package com.italtel.monitoring.fe.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.italtel.monitoring.fe.common.service.OMEntity;

@Entity
@Table(schema = "MONITORING")
@NamedQueries({
		@NamedQuery(name = InventoryService.QUERY_READ_SERVICE,
				query = "select l from InventoryService l where l.name=:name"),
		@NamedQuery(name = InventoryService.QUERY_READ_ALL_SERVICES,
				query = "select l from InventoryService l") })
@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryService implements OMEntity {

	private static final long serialVersionUID = 59477811476005071L;
	
	public static final String DEF_SERVICE_MONITORING = "ServiceMonitoring";
	public static final String DEF_SERVICE_DESCRIPTION ="Service that is related to the job Monitoring";
	
	public static final String QUERY_READ_SERVICE = "findServiceByName";
	public static final String QUERY_READ_ALL_SERVICES = "findAllServices";

	@Id
	private String name;

	private String description;

	@XmlTransient
	private String org;
	
	@JsonProperty("jobs")
	@XmlElementWrapper(name = "jobs")
	@XmlElement(name = "job")
	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
			orphanRemoval = true)
	@JoinTable(schema = "MONITORING")
	private List<InventoryServiceJob> jobs;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<InventoryServiceJob> getJobs() {
		return jobs;
	}

	public void setJobs(List<InventoryServiceJob> jobs) {
		if (jobs == null) {
			jobs = new ArrayList<InventoryServiceJob>();
		}
		this.jobs = jobs;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	@Override
	public String toString() {
		return "InventoryService [name=" + name + ", description=" + description + ", org=" + org + ", jobs=" + jobs
				+ "]";
	}
	
}

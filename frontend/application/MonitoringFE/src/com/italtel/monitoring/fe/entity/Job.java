package com.italtel.monitoring.fe.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
		@NamedQuery(name = Job.QUERY_READ_JOB,
				query = "select j from Job j where j.name=:name"),
		@NamedQuery(name = Job.QUERY_READ_ALL_JOBS,
				query = "select j from Job j") })
@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)
public class Job implements OMEntity {

	private static final long serialVersionUID = 59477811476005071L;

	public static final String  DEF_JOB_MONITORING = "Monitoring";
	public static final String  DEF_JOB_DESCRIPTION ="Job that monitors the node of 5G-Monitoring for system metrics";

	public static final String QUERY_READ_JOB = "findJobByName";
	public static final String QUERY_READ_ALL_JOBS = "findAllJobs";

	@Id
	private String name;

	private String description;
	
	@XmlTransient
	private String org;
	
	@Column(nullable = false)
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "sourceType", column = @Column(
					name = "sourceType", nullable = true)),
			@AttributeOverride(name = "metricPath", column = @Column(
					name = "metricPath", nullable = true)) ,
			@AttributeOverride(name = "interval", column = @Column(
					name = "interval", nullable = true)),
			@AttributeOverride(name = "port", column = @Column(
					name = "port", nullable = true)),
			@AttributeOverride(name = "dashboardType", column = @Column(
					name = "dashboardType", nullable = true))})
	
	private Source jobSource;

	@JsonProperty("nodes")
	@XmlElementWrapper(name = "nodes")
	@XmlElement(name = "node")
	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
			orphanRemoval = true)
	@JoinTable(schema = "MONITORING")
	private List<JobInventoryNode> jobNodes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Source getJobSource() {
		return jobSource;
	}

	public void setJobSource(Source jobSource) {
		this.jobSource = jobSource;
	}

	public List<JobInventoryNode> getJobNodes() {
		return jobNodes;
	}

	public void setJobNodes(List<JobInventoryNode> jobNodes) {
		if (jobNodes == null) {
			jobNodes = new ArrayList<JobInventoryNode>();
		}
		this.jobNodes = jobNodes;
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
		return "Job [name=" + name + ", description=" + description + ", org=" + org + ", jobSource=" + jobSource
				+ ", jobNodes=" + jobNodes + "]";
	}

}

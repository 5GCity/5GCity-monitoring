package com.italtel.monitoring.fe.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.italtel.monitoring.fe.common.service.OMEntity;

@Entity
@Table(schema = "MONITORING")
@NamedQueries({
	@NamedQuery(
		name = InventoryServiceJob.QUERY_COUNT_SERVICEJOB_WITH_JOB,
		query = "select count(sj) from InventoryServiceJob sj where sj.job.name=:name")
	})
 
@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryServiceJob implements OMEntity {

	private static final long serialVersionUID = -741610482278385814L;

	public static final String QUERY_COUNT_SERVICEJOB_WITH_JOB = "countServiceJobWithJob";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "SEQ_SERVICEJOB")
	@SequenceGenerator(name = "SEQ_SERVICEJOB",
			sequenceName = "SEQ_SERVICEJOB", initialValue = 0,
			allocationSize = 1, schema = "MONITORING")
	private Integer id;

	@XmlTransient
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST },
			fetch = FetchType.EAGER)
	private Job job;

	@XmlElement(required = true, nillable = false, name = "name")
	public String getJobName() {
		if (job != null) {
			return job.getName();
		}
		return null;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void setJobName(String jobName) {
		if (job == null) {
			job = new Job();
		}
		job.setName(jobName);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}

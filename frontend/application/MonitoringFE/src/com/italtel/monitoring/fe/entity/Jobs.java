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
public class Jobs implements Serializable {

	private static final long serialVersionUID = -8372241727494839580L;
	
	@JsonProperty("jobs")
	@XmlElementWrapper(name = "jobs")
	@XmlElement(name = "job")
	private List<Job> jobs;

	public Jobs() {
	}

	public Jobs(List<Job> jobs) {
		this.jobs = jobs;
	}

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Job [jobs=")
				.append(jobs).append("]");
		return builder.toString();
	}

}

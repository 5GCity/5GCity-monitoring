package com.italtel.monitoring.fe.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.italtel.monitoring.fe.common.service.OMEntity;

@Entity
@Table(schema = "MONITORING")
@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)

public class DashboardType implements OMEntity {

	private static final long serialVersionUID = 5710909135408702252L;

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE,
//			generator = "SEQ_DASHBOARDT")
//	@SequenceGenerator(name = "SEQ_DASHBOARDT",
//			sequenceName = "SEQ_DASHBOARDT", initialValue = 0,
//			allocationSize = 1, schema = "MONITORING")
//	private Integer id;

	@XmlElement(nillable = false, required = true)
	private String type;


//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
//
//	@Override
//	public String toString() {
//		return "DashboardType [id=" + id + ", type=" + type + "]";
//	}

	@Override
	public String toString() {
		return "DashboardType [type=" + type + "]";
	}

}

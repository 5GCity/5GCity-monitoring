package com.italtel.monitoring.fe.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.italtel.monitoring.fe.common.service.OMEntity;

@Entity
@Table(schema = "MONITORING")
@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
		@NamedQuery(name = NotificationMetric.QUERY_READ_ALL_NOTIF_METRICS_BY_NAME,
				query = "select m from NotificationMetric m where m.name=:name"),
		@NamedQuery(name = NotificationMetric.QUERY_READ_ALL_NOTIF_METRICS,
				query = "select m from NotificationMetric m"), })
public class NotificationMetric implements OMEntity {

	private static final long serialVersionUID = -954518167306317929L;

	public static final String QUERY_READ_ALL_NOTIF_METRICS_BY_NAME = "findAllNotifMetricByName";
	public static final String QUERY_READ_ALL_NOTIF_METRICS = "findAllNotifMetrics";

	
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "SEQ_COUNTER")
	@SequenceGenerator(name = "SEQ_COUNTER", sequenceName = "SEQ_COUNTER",
			initialValue = 1, allocationSize = 1, schema = "MONITORING")
	@Id
	private Long id;


	@XmlElement(nillable = false, required = true)
	private String name;
	

	// service della misura
	//@XmlTransient
	private String serviceName;
	
	// nodo della misura
	//@XmlTransient
	private String ipAddress;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar time;

	// Valore del contatore e quindi della misura
	private double value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "NotificationMetric [id=" + id + ", name=" + name + ", serviceName=" + serviceName + ", ipAddress="
				+ ipAddress + ", time=" + time + ", value=" + value + "]";
	}
	

}

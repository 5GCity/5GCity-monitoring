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
@NamedQueries({ @NamedQuery(
		name = InventoryServiceInventoryMetric.QUERY_COUNT_SERVICENODE_WITH_METRIC,
		query = "select count(sm) from InventoryServiceInventoryMetric sm where sm.metric.name=:name") })
@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryServiceInventoryMetric implements OMEntity {

	private static final long serialVersionUID = -741610482278385814L;

	public static final String QUERY_COUNT_SERVICENODE_WITH_METRIC = "countServiceNodeWithMetric";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "SEQ_SERVICEMETRIC")
	@SequenceGenerator(name = "SEQ_SERVICEMETRIC",
			sequenceName = "SEQ_SERVICEMETRIC", initialValue = 0,
			allocationSize = 1, schema = "MONITORING")
	private Integer id;

	@XmlTransient
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST },
			fetch = FetchType.EAGER)
	private InventoryMetric metric;

	@XmlElement(required = true, nillable = false, name = "name")
	public String getInventoryMetricName() {
		if (metric != null) {
			return metric.getName();
		}
		return null;
	}

	public InventoryMetric getMetric() {
		return metric;
	}

	public void setMetric(InventoryMetric metric) {
		this.metric = metric;
	}

	public void setInventoryMetricName(String metricName) {
		if (metric == null) {
			metric = new InventoryMetric();
		}
		metric.setName(metricName);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}

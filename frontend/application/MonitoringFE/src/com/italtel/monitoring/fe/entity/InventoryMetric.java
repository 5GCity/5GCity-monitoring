package com.italtel.monitoring.fe.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@NamedQueries({
		@NamedQuery(name = InventoryMetric.QUERY_READ_METRIC,
				query = "select m from InventoryMetric m where m.name=:name"),
		@NamedQuery(name = InventoryMetric.QUERY_READ_ALL_METRICS,
				query = "select m from InventoryMetric m"), })
public class InventoryMetric implements OMEntity {

	private static final long serialVersionUID = -954518167306317929L;

	public static final String QUERY_READ_METRIC = "findMetricByName";
	public static final String QUERY_READ_ALL_METRICS = "findAllMetrics";

	@XmlElement(nillable = false, required = true)
	@Id
	private String name;

	@XmlElement(nillable = false, required = true)
	private String desc;

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InventoryMetrics [name=").append(name)
				.append(", desc=").append(desc).append("]");
		return builder.toString();
	}

}

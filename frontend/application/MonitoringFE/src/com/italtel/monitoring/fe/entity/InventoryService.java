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

	public static final String QUERY_READ_SERVICE = "findServiceByName";
	public static final String QUERY_READ_ALL_SERVICES = "findAllServices";

	@Id
	private String name;

	private String description;

	private int interval;

	@JsonProperty("nodes")
	@XmlElementWrapper(name = "nodes")
	@XmlElement(name = "node")
	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
			orphanRemoval = true)
	@JoinTable(schema = "MONITORING")
	private List<InventoryServiceInventoryNode> inventoryNodes;

	@JsonProperty("metrics")
	@XmlElementWrapper(name = "metrics")
	@XmlElement(name = "metric")
	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
			orphanRemoval = true)
	@JoinTable(schema = "MONITORING")
	private List<InventoryServiceInventoryMetric> inventoryMetrics;

	public List<InventoryServiceInventoryMetric> getInventoryMetrics() {
		return inventoryMetrics;
	}

	public void setInventoryMetrics(
			List<InventoryServiceInventoryMetric> inventoryMetrics) {
		if (inventoryMetrics == null) {
			inventoryMetrics = new ArrayList<InventoryServiceInventoryMetric>();
		}
		this.inventoryMetrics = inventoryMetrics;
	}

	public String getDescription() {
		return description;
	}

	public int getInterval() {
		return interval;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<InventoryServiceInventoryNode> getInventoryNodes() {
		return inventoryNodes;
	}

	public void setInventoryNodes(
			List<InventoryServiceInventoryNode> inventoryNodes) {
		if (inventoryNodes == null) {
			inventoryNodes = new ArrayList<InventoryServiceInventoryNode>();
		}
		this.inventoryNodes = inventoryNodes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InventoryService [name=").append(name)
		// .append(", inventoryMetrics=").append(metrics)
				.append(", inventoryNodes=").append(inventoryNodes).append("]");
		return builder.toString();
	}
}

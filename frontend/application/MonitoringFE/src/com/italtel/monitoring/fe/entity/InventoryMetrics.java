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
public class InventoryMetrics implements Serializable {

	private static final long serialVersionUID = 3629514493712724392L;

	@JsonProperty("inventoryMetrics")
	@XmlElementWrapper(name = "inventoryMetrics")
	@XmlElement(name = "inventoryMetric")
	private List<InventoryMetric> inventoryMetrics;

	public InventoryMetrics() {
	}

	public InventoryMetrics(List<InventoryMetric> inventoryMetrics) {
		this.inventoryMetrics = inventoryMetrics;
	}

	public List<InventoryMetric> getInventoryMetrics() {
		return inventoryMetrics;
	}

	public void setInventoryMetrics(List<InventoryMetric> inventoryMetrics) {
		this.inventoryMetrics = inventoryMetrics;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InventoryMetrics [inventoryMetrics=")
				.append(inventoryMetrics).append("]");
		return builder.toString();
	}

}

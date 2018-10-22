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
public class InventoryServices implements Serializable {

	private static final long serialVersionUID = 3629514493712724392L;

	@JsonProperty("inventoryServices")
	@XmlElementWrapper(name = "inventoryServices")
	@XmlElement(name = "inventoryService")
	private List<InventoryService> inventoryServices;

	public InventoryServices() {
	}

	public InventoryServices(List<InventoryService> inventoryServices) {
		this.inventoryServices = inventoryServices;
	}

	public List<InventoryService> getInventoryServices() {
		return inventoryServices;
	}

	public void setInventoryServices(List<InventoryService> inventoryServices) {
		this.inventoryServices = inventoryServices;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InventoryServices [inventoryServices=")
				.append(inventoryServices).append("]");
		return builder.toString();
	}

}

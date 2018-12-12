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
public class InventoryNodes implements Serializable {

	private static final long serialVersionUID = 3629514493712724392L;

	@JsonProperty("inventoryNodes")
	@XmlElementWrapper(name = "inventoryNodes")
	@XmlElement(name = "inventoryNode")
	private List<InventoryNode> inventoryNodes;

	public InventoryNodes() {
	}

	public InventoryNodes(List<InventoryNode> inventoryNodes) {
		this.inventoryNodes = inventoryNodes;
	}

	public List<InventoryNode> getInventoryNodes() {
		return inventoryNodes;
	}

	public void setInventoryNodes(List<InventoryNode> inventoryNodes) {
		this.inventoryNodes = inventoryNodes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InventoryNodes [inventoryNodes=")
				.append(inventoryNodes).append("]");
		return builder.toString();
	}

}

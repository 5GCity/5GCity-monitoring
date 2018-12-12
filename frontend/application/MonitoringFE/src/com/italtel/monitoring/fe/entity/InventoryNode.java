package com.italtel.monitoring.fe.entity;

import javax.persistence.Column;
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
		@NamedQuery(
				name = InventoryNode.QUERY_READ_NODE,
				query = "select node from InventoryNode node where node.name=:name"),
		@NamedQuery(name = InventoryNode.QUERY_READ_ALL_NODES,
				query = "select node from InventoryNode node"), })
public class InventoryNode implements OMEntity {

	private static final long serialVersionUID = 5710909135408702252L;

	public static final String QUERY_READ_NODE = "findNodeByName";
	public static final String QUERY_READ_ALL_NODES = "findAllNodes";

	@XmlElement(nillable = false, required = true)
	@Id
	private String name;

	@XmlElement(nillable = false, required = true)
	private String ip;

	@XmlElement(nillable = false, required = true)
	@Column(nullable = false)
	private Integer port;

	public String getIp() {
		return ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

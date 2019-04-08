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
import javax.xml.bind.annotation.XmlTransient;

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
	
	public static final String DEF_NODE_MONITORING = "Monitoring";
	
	@XmlElement(nillable = false, required = true)
	@Id
	private String name;

	@XmlElement(nillable = false, required = true)
	private String ip;

	@XmlTransient
	private String org;
	
	public String getIp() {
		return ip;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "InventoryNode [name=" + name + ", ip=" + ip + ", org=" + org + "]";
	}

	
}

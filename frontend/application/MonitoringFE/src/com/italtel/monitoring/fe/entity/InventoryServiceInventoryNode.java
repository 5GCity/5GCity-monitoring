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
		name = InventoryServiceInventoryNode.QUERY_COUNT_SERVICENODE_WITH_NODE,
		query = "select count(sn) from InventoryServiceInventoryNode sn where sn.node.name=:name") })
@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryServiceInventoryNode implements OMEntity {

	private static final long serialVersionUID = -741610482278385814L;

	public static final String QUERY_COUNT_SERVICENODE_WITH_NODE = "countServiceNodeWithNode";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "SEQ_SERVICENODE")
	@SequenceGenerator(name = "SEQ_SERVICENODE",
			sequenceName = "SEQ_SERVICENODE", initialValue = 0,
			allocationSize = 1, schema = "MONITORING")
	private Integer id;

	@XmlTransient
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST },
			fetch = FetchType.EAGER)
	private InventoryNode node;

	@XmlElement(required = true, nillable = false, name = "name")
	public String getInventoryNodeName() {
		if (node != null) {
			return node.getName();
		}
		return null;
	}

	public void setInventoryNodeName(String nodeName) {
		if (node == null) {
			node = new InventoryNode();
		}
		node.setName(nodeName);
	}

	public Integer getId() {
		return id;
	}

	public InventoryNode getNode() {
		return node;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNode(InventoryNode node) {
		this.node = node;
	}

}

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
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.italtel.monitoring.fe.common.service.OMEntity;

@Entity
@Table(schema = "MONITORING")
@NamedQueries({
		@NamedQuery(name = Slice.QUERY_READ_SLICE,
				query = "select l from Slice l where l.name=:name"),
		@NamedQuery(name = Slice.QUERY_READ_ALL_SLICES,
				query = "select l from Slice l") })
@XmlRootElement(namespace = "http://italtel.com/OaM/conf")
@XmlAccessorType(XmlAccessType.FIELD)
public class Slice implements OMEntity {

	private static final long serialVersionUID = 59477811476005071L;

	public static final String QUERY_READ_SLICE = "findSliceByName";
	public static final String QUERY_READ_ALL_SLICES = "findAllSlices";
	public static final String DEF_SLICE_MONITORING = "Monitoring";
	
	@Id
	private String name;

	@JsonProperty("nodes")
	@XmlElementWrapper(name = "nodes")
	@XmlElement(name = "node")
	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
			orphanRemoval = true)
	@JoinTable(schema = "MONITORING")
	private List<SliceInventoryNode> sliceNodes;

	@XmlTransient
	private String org;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public List<SliceInventoryNode> getSliceNodes() {
		return sliceNodes;
	}

	public void setSliceNodes(List<SliceInventoryNode> sliceNodes) {
		if (sliceNodes == null) {
			sliceNodes = new ArrayList<SliceInventoryNode>();
		}
		this.sliceNodes = sliceNodes;
	}

	@Override
	public String toString() {
		return "Slice [name=" + name + ", sliceNodes=" + sliceNodes + ", org=" + org + "]";
	}
	
}

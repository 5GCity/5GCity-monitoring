package com.italtel.monitoring.fe.entity.um;


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
import com.italtel.monitoring.fe.entity.Slice;

@Entity
@Table(schema = "MONITORING")
@NamedQueries({ @NamedQuery(
		name = UserSlice.QUERY_COUNT_USERSLICE_WITH_SLICE,
		query = "select count(us) from UserSlice us where us.slice.name=:name") })
@XmlRootElement(namespace = "http://italtel.com/OaM/um")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserSlice implements OMEntity {

	private static final long serialVersionUID = -741610482278385814L;

	public static final String QUERY_COUNT_USERSLICE_WITH_SLICE = "countUserSliceWithSlice";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "SEQ_USERSLICE")
	@SequenceGenerator(name = "SEQ_USERSLICE",
			sequenceName = "SEQ_USERSLICE", initialValue = 0,
			allocationSize = 1, schema = "MONITORING")
	private Integer id;

	@XmlTransient
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST },
			fetch = FetchType.EAGER)
	private Slice slice;

	@XmlElement(required = true, nillable = false, name = "name")
	public String getSliceName() {
		if (slice != null) {
			return slice.getName();
		}
		return null;
	}

	public void setSliceName(String sliceName) {
		if (slice == null) {
			slice = new Slice();
		}
		slice.setName(sliceName);
	}

	public Integer getId() {
		return id;
	}

	public Slice getSlice() {
		return slice;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSlice(Slice slice) {
		this.slice = slice;
	}

}

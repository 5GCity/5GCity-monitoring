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
public class Slices implements Serializable {

	private static final long serialVersionUID = 3629514493712724392L;

	@JsonProperty("slices")
	@XmlElementWrapper(name = "slices")
	@XmlElement(name = "slice")
	private List<Slice> slices;

	public Slices() {
	}

	public Slices(List<Slice> slices) {
		this.slices = slices;
	}

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Slice [slices=")
				.append(slices).append("]");
		return builder.toString();
	}

}

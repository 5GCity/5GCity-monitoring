package com.italtel.monitoring.fe.ws.um.entity;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.italtel.monitoring.fe.entity.um.User;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Users implements Serializable {

	private static final long serialVersionUID = -7974914582250552217L;

	@JsonProperty("users")
	@XmlElementWrapper(name = "users")
	@XmlElement(name = "user")
	private List<User> users;

	public Users() {
	}

	public Users(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Users [users=").append(users).append("]");
		return builder.toString();
	}

}

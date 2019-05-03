package com.italtel.monitoring.fe.entity.um;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.italtel.monitoring.fe.common.service.OMEntity;
import com.italtel.monitoring.fe.utils.xml.PasswordAdapter;

@Entity
@Table(schema = "MONITORING")
@NamedQueries({
		@NamedQuery(name = User.QUERY_READ_USER_BY_USERNAME,
				query = "select u from User u where u.username=:username"),
		@NamedQuery(name = User.QUERY_READ_USER_BY_ROLE,
				query = "select u from User u where u.role=:role"),
		@NamedQuery(name = User.QUERY_READ_ALL_USERS,
				query = "select u from User u"),
		@NamedQuery(name = User.QUERY_COUNT_USERS_WITH_SELECTED_ROLE,
				query = "select count(u) from User u where u.role=:role"),	 })
@XmlRootElement(namespace = "http://italtel.com/OaM/um")
@XmlAccessorType(XmlAccessType.FIELD)

public class User implements OMEntity {

	private static final long serialVersionUID = -2267895037325622320L;

	public static final String DEF_SYSTEM_ORG = "SYSMONITORING";
	public static final String DEF_ADMIN_ORG = "MONITORING";
	public static final String QUERY_READ_USER_BY_USERNAME = "findUserByUsername";
	public static final String QUERY_READ_USER_BY_ROLE = "findUserByRole";
	public static final String QUERY_READ_ALL_USERS = "findAllUsers";
	public static final String QUERY_COUNT_USERS_WITH_SELECTED_ROLE = "countUsersWithSelectedRole";

	@Id
	@XmlElement(nillable = false, required = true)
	private String username;
	
	@XmlJavaTypeAdapter(PasswordAdapter.class)
	private String passwd;
	
	@Enumerated(EnumType.STRING)
	private RoleType role;
	
	private String org;
	
	@JsonProperty("slices")
	@XmlElementWrapper(name = "slices")
	@XmlElement(name = "slice")
	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
			orphanRemoval = false)
	@JoinTable(schema = "MONITORING")
	private List<UserSlice> userSlices;

	public User() {
	}

	public User(String username, String passwd, RoleType role, String org, List<UserSlice> userSlices) {
		this.username = username;
		this.passwd = passwd;
		this.role = role;
		this.org = org;
		this.userSlices = userSlices;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}
	
	
	public List<UserSlice> getUserSlices() {
		return userSlices;
	}

	public void setUserSlices(List<UserSlice> userSlices) {
		if (userSlices == null) {
			userSlices = new ArrayList<UserSlice>();
		}
		this.userSlices = userSlices;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", passwd=" + passwd + ", role=" + role + ", org=" + org + ", userSlices="
				+ userSlices + "]";
	}


}

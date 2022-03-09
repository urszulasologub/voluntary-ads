package com.example.announcements.models;


import javax.persistence.*;

@Entity
@Table(name = "auth_role")
public class Role {

	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	@Column(name = "auth_role_id")
	private Integer id;

	@Column(name = "role_name", unique = true)
	private String role;

	@Column(name = "role_desc")
	private String desc;

	public Role() {
		this(null, null, null);
	}

	public Role(Integer id, String role, String desc) {
		this.id = id;
		this.role = role;
		this.desc = desc;
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}
}

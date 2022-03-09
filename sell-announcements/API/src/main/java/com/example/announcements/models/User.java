package com.example.announcements.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auth_user")
public class User {

	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	@Column(name = "auth_user_id")
	private Integer id;

	@NotNull(message = "Email is compulsory")
	@Email(message = "Email is invalid")
	@Column(name = "email", unique=true)
	private String email;

	@NotNull(message="Password is compulsory")
	@Length(min=5, message="Password should be at least 5 characters")
	@Column(name = "password")
	@JsonIgnore
	private String password;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "auth_user_role", joinColumns = @JoinColumn(name = "auth_user_id"), inverseJoinColumns = @JoinColumn(name = "auth_role_id"))
	private Set<Role> roles = new HashSet<Role>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="user_id")
	private Set<Announcement> announcements = new HashSet<Announcement>();

	public User() {
		this(null, null, null, null, null);
	}

	public User(Integer id, @NotNull(message = "Email is compulsory") @Email(message = "Email is invalid") String email, @NotNull(message = "Password is compulsory") @Length(min = 5, message = "Password should be at least 5 characters") String password, Set<Role> roles, Set<Announcement> announcements) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.announcements = announcements;
	}

	public Set<Announcement> getAnnouncements() {
		return announcements;
	}

	public void setAnnouncements(Set<Announcement> announcements) {
		this.announcements = announcements;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}

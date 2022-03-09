package com.example.announcements.models;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="category")
public class Category {

	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	@Column(name = "category_id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@OneToMany(cascade = CascadeType.DETACH)
	@JoinColumn(name="category_id")
	private Set<Announcement> announcements = new HashSet<Announcement>();

	public Category() {
		this(null, null, null);
	}

	public Category(Integer id, String name, Set<Announcement> announcements) {
		this.id = id;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

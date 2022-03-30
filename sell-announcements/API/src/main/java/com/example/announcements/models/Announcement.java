package com.example.announcements.models;


import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "announcement")
public class Announcement implements Comparable<Announcement> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "announcement_id")
    private Integer id;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Category categoryId;

    @ManyToOne(cascade = CascadeType.DETACH)
    private User userId;

    @Column(name = "announcement_name", length = 50)
    private String name;

    @Column(name = "quantity")
    private Float quantity;

    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "is_hidden")
    private Boolean is_hidden = false;

    @Column(name = "phone_number", length = 15)
    private String phone_number;

    @Column(name = "datetime")
    private Date datetime = Calendar.getInstance().getTime();

    @Column(name = "location")
    private String location;

    @OneToMany(cascade = CascadeType.DETACH)
    @JoinColumn(name = "announcement_id")
    private Set<PrivateMessage> privateMessages = new HashSet<PrivateMessage>();

    public Announcement() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

    public Announcement(Integer id, Category category_id, User user_id, String name, Float quantity, String description, Boolean is_hidden, String phone_number, Date datetime, String location) {
        this.id = id;
        this.categoryId = category_id;
        this.userId = user_id;
        this.name = name;
        this.quantity = quantity;
        this.description = description;
        this.is_hidden = is_hidden;
        this.phone_number = phone_number;
        this.datetime = datetime;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Category getCategory_id() {
        return categoryId;
    }

    public void setCategory_id(Category category_id) {
        this.categoryId = category_id;
    }

    public User getUser_id() {
        return userId;
    }

    public void setUser_id(User user_id) {
        this.userId = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIs_hidden() {
        return is_hidden;
    }

    public void setIs_hidden(Boolean is_hidden) {
        this.is_hidden = is_hidden;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<PrivateMessage> getPrivateMessages() {
        return privateMessages;
    }

    public void setPrivateMessages(Set<PrivateMessage> privateMessages) {
        this.privateMessages = privateMessages;
    }

    @Override
    public int compareTo(Announcement o) {
        if (o == null || o.getDatetime() == null) return -1;
        else if (this.getDatetime() == null) return 1;
        return (-this.getDatetime().compareTo(o.getDatetime()));
    }
}

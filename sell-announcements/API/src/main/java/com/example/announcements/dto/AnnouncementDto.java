package com.example.announcements.dto;

import com.example.announcements.models.Announcement;
import com.example.announcements.models.PrivateMessage;
import com.example.announcements.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Set;

public class AnnouncementDto extends Announcement {

    private Integer categoryId;
    private String description;

    @JsonProperty("categoryId")
    public Integer getIntegerCategory_id() {
        return categoryId;
    }

    @JsonProperty("categoryId")
    public void setIntegerCategory_id(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(Integer id) {
        super.setId(id);
    }


    @Override
    public User getUser_id() {
        return super.getUser_id();
    }

    @Override
    public void setUser_id(User user_id) {
        super.setUser_id(user_id);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public Float getQuantity() {
        return super.getQuantity();
    }

    @Override
    public void setQuantity(Float quantity) {
        super.setQuantity(quantity);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Boolean getIs_hidden() {
        return super.getIs_hidden();
    }

    @Override
    public void setIs_hidden(Boolean is_hidden) {
        super.setIs_hidden(is_hidden);
    }

    @Override
    public String getPhone_number() {
        return super.getPhone_number();
    }

    @Override
    public void setPhone_number(String phone_number) {
        super.setPhone_number(phone_number);
    }

    @Override
    public Date getDatetime() {
        return super.getDatetime();
    }

    @Override
    public void setDatetime(Date datetime) {
        super.setDatetime(datetime);
    }

    @Override
    public String getLocation() {
        return super.getLocation();
    }

    @Override
    public void setLocation(String location) {
        super.setLocation(location);
    }

    @Override
    public Set<PrivateMessage> getPrivateMessages() {
        return super.getPrivateMessages();
    }

    @Override
    public void setPrivateMessages(Set<PrivateMessage> privateMessages) {
        super.setPrivateMessages(privateMessages);
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer category_id) {
        this.categoryId = category_id;
    }

}
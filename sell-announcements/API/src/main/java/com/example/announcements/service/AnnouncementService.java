package com.example.announcements.service;

import com.example.announcements.models.Announcement;
import com.example.announcements.models.Category;
import com.example.announcements.models.User;

import java.util.List;

public interface AnnouncementService {

	public List<Announcement> getAllPublicAnnouncements();
	public List<Announcement> getPublicAnnouncementsInCategory(Category category_id);
	public Announcement getAnnouncementById(Integer id);
	public List<Announcement> getUsersPublicAnnouncements(User user_id);
	public List<Announcement> getUsersAnnouncements(User user_id);
}

package com.example.announcements.service;

import com.example.announcements.models.Announcement;
import com.example.announcements.models.Category;
import com.example.announcements.models.User;

import java.util.List;

public interface AnnouncementService {
	List<Announcement> getAllPublicAnnouncements();
	List<Announcement> getPublicAnnouncementsInCategory(Category category_id);
	Announcement getAnnouncementById(Integer id);
	List<Announcement> getUsersPublicAnnouncements(User user_id);
	List<Announcement> getUsersAnnouncements(User user_id);
}

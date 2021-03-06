package com.example.announcements.service;

import com.example.announcements.models.Announcement;
import com.example.announcements.models.Category;
import com.example.announcements.models.User;
import com.example.announcements.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnnouncementServiceImp implements AnnouncementService {

	@Autowired
	AnnouncementRepository announcementRepository;

	@Autowired
	UserService userService;

	@Override
	public List<Announcement> getAllPublicAnnouncements() {
		List <Announcement> announcements = announcementRepository.findAll();
		List <Announcement> public_announcements = new ArrayList<Announcement>();
		for (Announcement an : announcements) {
			if (!an.getIs_hidden())
				public_announcements.add(an);
		}
		Collections.sort(public_announcements);
		return public_announcements;
	}

	@Override
	public List<Announcement> getPublicAnnouncementsInCategory(Category category_id) {
		List <Announcement> announcements = announcementRepository.findByCategoryId(category_id);
		List <Announcement> public_announcements = new ArrayList<Announcement>();
		for (Announcement an : announcements) {
			if (!an.getIs_hidden())
				public_announcements.add(an);
		}
		Collections.sort(public_announcements);
		return public_announcements;
	}

	@Override
	public Announcement getAnnouncementById(Integer id) {
		Optional<Announcement> announcement = announcementRepository.findById(id);
		if (announcement.isPresent()) {
			if (announcement.get().getIs_hidden()) {
				User user = userService.getLoggedInUser();
				if (user == null)
					throw new RuntimeException("Cannot access private announcement while logged off!");
				else if (user != announcement.get().getUser_id())
					throw new RuntimeException("You don't have permission to see this announcement");
				return announcement.get();
			}
			return announcement.get();
		}
		throw new RuntimeException("Couldn't find an announcement");
	}

	@Override
	public List<Announcement> getUsersPublicAnnouncements(User user_id) {
		List<Announcement> announcements = new ArrayList<>();
		for (Announcement an : getUsersAnnouncements(user_id)) {
			if (!an.getIs_hidden())
				announcements.add(an);
		}
		Collections.sort(announcements);
		return announcements;
	}

	@Override
	public List<Announcement> getUsersAnnouncements(User user_id) {
		List<Announcement> announcements = announcementRepository.findByUserId(user_id);
		Collections.sort(announcements);
		return announcements;
	}


	//TODO too many arguments, maybe use Builder pattern?
	@Override
	public Announcement seedAnnouncement(Category category, String name, User user, String location, String phone_number, float quantity, String description, boolean isHidden, Date date) {
		Announcement announcement = new Announcement();
		announcement.setCategory_id(category);
		announcement.setName(name);
		announcement.setUser_id(user);
		announcement.setLocation(location);
		announcement.setPhone_number(phone_number);
		announcement.setQuantity(quantity);
		announcement.setDescription(description);
		announcement.setIs_hidden(isHidden);
		announcement.setDatetime(date);
		return saveAnnouncement(announcement);
	}

	@Override
	public Announcement saveAnnouncement(Announcement announcement) {
		return announcementRepository.save(announcement);
	}

}

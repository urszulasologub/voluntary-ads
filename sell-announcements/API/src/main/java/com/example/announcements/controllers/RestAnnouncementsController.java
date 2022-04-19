package com.example.announcements.controllers;

import com.example.announcements.dto.AnnouncementDto;
import com.example.announcements.models.Announcement;
import com.example.announcements.models.Category;
import com.example.announcements.models.PrivateMessage;
import com.example.announcements.models.User;
import com.example.announcements.repository.AnnouncementRepository;
import com.example.announcements.repository.CategoryRepository;
import com.example.announcements.repository.PrivateMessageRepository;
import com.example.announcements.repository.UserRepository;
import com.example.announcements.service.AnnouncementService;
import com.example.announcements.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class RestAnnouncementsController {

	@Autowired
	AnnouncementRepository announcementRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	AnnouncementService announcementService;

	@Autowired
	PrivateMessageRepository privateMessageRepository;


	@RequestMapping(value = { "/announcements" }, method = RequestMethod.GET)
	public List<Announcement> announcementList() {
		return announcementService.getAllPublicAnnouncements();
	}


	@RequestMapping(value = { "/announcements/category/{id}"}, method = RequestMethod.GET)
	public List<Announcement> announcementListInCategory(@PathVariable("id") Category category_id) {
		return announcementService.getPublicAnnouncementsInCategory(category_id);
	}


	@RequestMapping(value = { "/categories" }, method = RequestMethod.GET)
	public List<Category> CategoryList() {
		return categoryRepository.findAll();
	}

    @RequestMapping(value = { "/announcements/add" }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
    public Announcement saveAnnouncementJson(@RequestBody AnnouncementDto inputAnnouncement) throws IOException {
		inputAnnouncement.setIntegerCategory_id(inputAnnouncement.getCategoryId()); // map to multipart counterpart
        return saveAnnouncement(inputAnnouncement);
    }

	@RequestMapping(value = { "/announcements/add" }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, method = RequestMethod.POST)
	public Announcement saveAnnouncement( AnnouncementDto inputAnnouncement) throws IOException {
		User user = userService.getLoggedInUser();
		if (user == null)
			throw new RuntimeException("Not logged in");
		inputAnnouncement.setId(null);
		inputAnnouncement.setUser_id(user);
		inputAnnouncement.setIs_hidden(false);
		inputAnnouncement.setDatetime(new Date());
		if (inputAnnouncement.getQuantity() == null || inputAnnouncement.getQuantity() <= 0.0)
			throw new RuntimeException("Incorrect quantity");
		else if (inputAnnouncement.getCategoryId() == null)
			throw new RuntimeException("categoryId is missing");
		else if (inputAnnouncement.getLocation() == null)
			throw new RuntimeException("location is missing");
		else if (inputAnnouncement.getName() == null)
			throw new RuntimeException("name is missing");
		else if (inputAnnouncement.getDescription() == null)
			throw new RuntimeException("description is missing");


		Announcement newAnnouncement = new Announcement(
				inputAnnouncement.getId(),
				categoryRepository.findCategoryById(inputAnnouncement.getIntegerCategory_id()),
				user,
				inputAnnouncement.getName(),
				inputAnnouncement.getQuantity(),
				inputAnnouncement.getDescription(),
				inputAnnouncement.getIs_hidden(),
				inputAnnouncement.getPhone_number(),
				inputAnnouncement.getDatetime(),
				inputAnnouncement.getLocation()
		);
		return announcementRepository.save(newAnnouncement);
	}


	@RequestMapping(value = { "/announcements/{id}" }, method = RequestMethod.GET)
	public Announcement getAnnouncement(@PathVariable("id") Integer announcement_id) {
		return announcementService.getAnnouncementById(announcement_id);
	}


	@RequestMapping(value = { "/announcements/delete/{id}" }, method = RequestMethod.DELETE)
	public Map<String, String> deleteAnnouncement(@PathVariable("id") Integer announcement_id) {
		Map<String, String> result = new HashMap<>();
		Optional <Announcement> announcement = announcementRepository.findById(announcement_id);
		if (announcement.isPresent()) {
			User user = userService.getLoggedInUser();
			if (user == null)
				throw new RuntimeException("Not logged in");
			else if (user != announcement.get().getUser_id()) {
				result.put("result", "failure");
				return result;
			}
            List<PrivateMessage> messagesToDelete= privateMessageRepository.findByAnnouncementId(announcement.get());
            privateMessageRepository.deleteAll(messagesToDelete);
			announcementRepository.delete(announcement.get());
			result.put("result", "success");
		} else {
			result.put("result", "failure");
		}
		return result;
	}


	@RequestMapping(value = { "/announcements/hide/{announcement_id}" }, method = RequestMethod.PUT)
	public Announcement hideAnnouncement(@PathVariable("announcement_id") Integer announcement_id) {
		Optional<Announcement> announcement = announcementRepository.findById(announcement_id);
		if (announcement.isPresent()) {
			User user = userService.getLoggedInUser();
			if (user == null)
				throw new RuntimeException("Not logged in");
			else if (user != announcement.get().getUser_id())
				throw new RuntimeException("Cannot hide someone's announcement");
			announcement.get().setIs_hidden(true);
			return announcementRepository.save(announcement.get());
		}
		return null;
	}

	@RequestMapping(value = { "announcements/user/{user_id}" }, method = RequestMethod.GET)
	public List<Announcement> getUsersAnnouncements(@PathVariable("user_id") Integer user_id) {
		Optional<User> user = userRepository.findById(user_id);
		if (user.isPresent()) {
			if (userService.getLoggedInUser() == user.get())
				return announcementService.getUsersAnnouncements(user.get());
			return announcementService.getUsersPublicAnnouncements(user.get());
		}
		return null;
	}


	@RequestMapping(value = { "announcements/search/{keyword}" }, method = RequestMethod.GET)
	public List<Announcement> getFilteredAnnouncements(@PathVariable("keyword") String keyword) {
		List <Announcement> announcements = announcementService.getAllPublicAnnouncements();
		List <Announcement> filteredAnnouncements = new ArrayList<>();
		for (Announcement announcement : announcements) {
			if (announcement.getName().toLowerCase().contains(keyword.toLowerCase()) || announcement.getDescription().toLowerCase().contains(keyword.toLowerCase()))
				filteredAnnouncements.add(announcement);
		}
		return filteredAnnouncements;
	}
}

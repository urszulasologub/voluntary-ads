package com.example.announcements.service;

import com.example.announcements.models.Announcement;
import com.example.announcements.models.PrivateMessage;
import com.example.announcements.models.User;
import com.example.announcements.repository.AnnouncementRepository;
import com.example.announcements.repository.PrivateMessageRepository;
import com.example.announcements.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PrivateMessageServiceImp implements PrivateMessageService {

	@Autowired
	AnnouncementRepository announcementRepository;

	@Autowired
	UserService userService;

	@Autowired
	PrivateMessageRepository privateMessageRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public List<PrivateMessage> getConversationWithUser(Announcement announcement_id, User buyer_id) {
		User logged_user = userService.getLoggedInUser();
		List<PrivateMessage> announcements_priv = privateMessageRepository.findByAnnouncementId(announcement_id);
		List<PrivateMessage> conversation = new ArrayList<>();
		if (logged_user != buyer_id && logged_user != announcement_id.getUser_id())
			throw new RuntimeException("Can't access someone's private messages");
		for (PrivateMessage message : announcements_priv) {
			if (message.getBuyer() == buyer_id)
				conversation.add(message);
		}
		Collections.sort(conversation);
		return conversation;
	}


	@Override
	public List<User> getUsersWhoSentMessageToAnnouncement(Announcement announcement_id) {
		User logged_user = userService.getLoggedInUser();
		if (announcement_id.getUser_id() != logged_user)
			throw new RuntimeException("Can't access someone's private messages");
		List<PrivateMessage> messages = privateMessageRepository.findByAnnouncementId(announcement_id);
		List<User> users = new ArrayList<>();
		for (PrivateMessage message : messages) {
			users.add(message.getBuyer());
		}
		return users;
	}
}

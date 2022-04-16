package com.example.announcements.service;

import com.example.announcements.models.Announcement;
import com.example.announcements.models.PrivateMessage;
import com.example.announcements.models.User;

import java.util.List;

public interface PrivateMessageService {
	List<PrivateMessage> getConversationWithUser(Announcement announcement_id, User buyer_id);
	List<User> getUsersWhoSentMessageToAnnouncement(Announcement announcement_id);
}

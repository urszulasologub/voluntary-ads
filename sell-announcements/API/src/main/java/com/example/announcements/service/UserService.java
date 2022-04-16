package com.example.announcements.service;

import com.example.announcements.models.User;

public interface UserService {
	User saveUser(User user);
	boolean isUserAlreadyPresent(User user);
	User getLoggedInUser();
	User saveAdminUser(User user);
	User seedUser(String email, String password, boolean isAdmin);
}

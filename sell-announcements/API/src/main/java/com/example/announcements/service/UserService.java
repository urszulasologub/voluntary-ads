package com.example.announcements.service;

import com.example.announcements.models.User;

public interface UserService {
	public User saveUser(User user);
	public boolean isUserAlreadyPresent(User user);
	public User getLoggedInUser();
	public User saveAdminUser(User user);
}

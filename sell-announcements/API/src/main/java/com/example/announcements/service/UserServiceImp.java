package com.example.announcements.service;

import com.example.announcements.models.Category;
import com.example.announcements.models.Role;
import com.example.announcements.models.User;
import com.example.announcements.repository.CategoryRepository;
import com.example.announcements.repository.RoleRepository;
import com.example.announcements.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;


@Service
public class UserServiceImp implements UserService {
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CategoryRepository categoryRepository;

	@PostConstruct
	private void postConstruct() {
		seedDefaultUserRoles();
		seedDefaultUsers();
		seedDefaultCategory("Default Advertisement");
	}

	private void seedDefaultUserRoles() {
		if (roleRepository.count() == 0) {
			Role admin = new Role();
			admin.setRole("ADMIN_USER");
			Role siteUser = new Role();
			siteUser.setRole("SITE_USER");
			roleRepository.save(admin);
			roleRepository.save(siteUser);
		}
	}

	private void seedDefaultUsers() {
		if (userRepository.count() == 0) {
			seedUser("admin@example.com", "admin123", true);
			seedUser("user@example.com", "admin123", false);
		}
	}

	private void seedUser(String email, String password, boolean isAdmin) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		if (isAdmin) {
			saveAdminUser(user);
		} else {
			saveUser(user);
		}
	}

	private void seedDefaultCategory(String name) {
		Category category = new Category();
		category.setName(name);
		categoryRepository.save(category);
	}

	@Override
	public User saveUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByRole("SITE_USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		if (isUserAlreadyPresent(user))
			throw new RuntimeException("E-mail already exists!");
		return userRepository.save(user);
	}

	@Override
	public User saveAdminUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		Set<Role> roles = new HashSet<>(roleRepository.findAll());
		user.setRoles(roles);
		user.setRoles(new HashSet<Role>(roles));
		if (isUserAlreadyPresent(user))
			throw new RuntimeException("E-mail already exists!");
		return userRepository.save(user);
	}

	@Override
	public boolean isUserAlreadyPresent(User user) {
		Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
		return existingUser.isPresent();
	}

	@Override
	public User getLoggedInUser() {
		Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
		if (currentAuthentication != null && currentAuthentication.isAuthenticated()) {
			String username;
			if (currentAuthentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
				username = ((org.springframework.security.core.userdetails.User) currentAuthentication.getPrincipal()).getUsername();
			} else {
				username = (String) currentAuthentication.getPrincipal();
			}

			Optional<User> userOptional = userRepository.findUserByEmail(username);
			return userOptional.orElse(null);
		} else {
			return null;
		}
	}
}

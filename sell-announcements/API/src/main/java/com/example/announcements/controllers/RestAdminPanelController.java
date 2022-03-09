package com.example.announcements.controllers;


import com.example.announcements.models.*;
import com.example.announcements.repository.*;
import com.example.announcements.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
public class RestAdminPanelController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PrivateMessageRepository privateMessageRepository;

    @Autowired
    RoleRepository roleRepository;

    @RequestMapping(value = { "admin/statistics"}, method = RequestMethod.GET)
    public Map<LocalDate, Integer> getStatisticsFromThisWeek() {
		Map<LocalDate, Integer> result = new TreeMap<>();
		LocalDate today = LocalDate.now();
		LocalDate weekAgo = LocalDate.now().minusDays(6);
		for (LocalDate date = weekAgo; date.isBefore(today.plusDays(1)); date = date.plusDays(1)) {
			result.put(date, 0);
		}
		for (Announcement ann : announcementRepository.findAll()) {
			for (LocalDate date = weekAgo; date.isBefore(today.plusDays(1)); date = date.plusDays(1)) {
			    try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(ann.getDatetime());
                    LocalDate announcementDate = LocalDate.of(cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH) + 1,
                            cal.get(Calendar.DAY_OF_MONTH));
                    if (announcementDate.compareTo(date) == 0) {
                        result.put(date, result.get(date) + 1);
                    }
                } catch (Exception e) {
			        e.printStackTrace();
                }
			}
		}
        return result;
    }


    @RequestMapping(value = { "/admin/create_database"}, method = RequestMethod.POST)
    public Map<String, String> createExampleDatabase() {
        Map<String, String> result = new HashMap<>();
        try {
            privateMessageRepository.deleteAll();
            privateMessageRepository.flush();
            announcementRepository.deleteAll();
            announcementRepository.flush();
            categoryRepository.deleteAll();
            categoryRepository.flush();
            userRepository.deleteAll();
            userRepository.flush();
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword("admin123");
            userService.saveAdminUser(admin);
            User user = new User();
            user.setEmail("user@example.com");
            user.setPassword("user123");
            userService.saveUser(user);
            Category phonesCategory = new Category();
            phonesCategory.setName("Phones");
            categoryRepository.save(phonesCategory);
            Category laptopsCategory = new Category();
            laptopsCategory.setName("Laptops");
            categoryRepository.save(laptopsCategory);

            Announcement iPhoneAnnouncement = new Announcement();
            iPhoneAnnouncement.setCategory_id(phonesCategory);
            iPhoneAnnouncement.setName("Apple iPhone XS");
            iPhoneAnnouncement.setUser_id(admin);
            iPhoneAnnouncement.setLocation("Lodz");
            iPhoneAnnouncement.setPhone_number("123456789");
            iPhoneAnnouncement.setPrice((float) 650.0);
            iPhoneAnnouncement.setDescription("The good The iPhone XS has a markedly improved dual camera, delivering better photos than the iPhone X in both dark and high-contrast environments. It has a faster processor, faster face ID. 512GB");
            iPhoneAnnouncement.setIs_hidden(false);
            iPhoneAnnouncement.setDatetime(new Date());
            announcementRepository.save(iPhoneAnnouncement);

            Announcement iPhoneAnnouncement2 = new Announcement();
            iPhoneAnnouncement2.setCategory_id(phonesCategory);
            iPhoneAnnouncement2.setName("APPLE iPhone 7 CHEAP");
            iPhoneAnnouncement2.setUser_id(user);
            iPhoneAnnouncement2.setLocation("Warsaw");
            iPhoneAnnouncement2.setPhone_number("123000789");
            iPhoneAnnouncement2.setPrice((float) 200.0);
            iPhoneAnnouncement2.setDescription("Very good price for a very good phone!");
            iPhoneAnnouncement2.setIs_hidden(false);
            iPhoneAnnouncement2.setDatetime(new Date());
            announcementRepository.save(iPhoneAnnouncement2);

            Announcement macbookAnnouncement = new Announcement();
            macbookAnnouncement.setCategory_id(laptopsCategory);
            macbookAnnouncement.setName("Macbook Air 2020 512GB");
            macbookAnnouncement.setUser_id(admin);
            macbookAnnouncement.setLocation("Lodz");
            macbookAnnouncement.setPhone_number("123456789");
            macbookAnnouncement.setPrice((float) 2000.0);
            macbookAnnouncement.setDescription("Newest Macbook Air. 512GB of storage, i5 processor, 13\" with new Magic Keyboard. Almost new, only 90 battery cycles");
            macbookAnnouncement.setIs_hidden(false);
            macbookAnnouncement.setDatetime(new Date());
            announcementRepository.save(macbookAnnouncement);

            Announcement laptopAnnouncement = new Announcement();
            laptopAnnouncement.setCategory_id(laptopsCategory);
            laptopAnnouncement.setName("Gaming notebook MSI");
            laptopAnnouncement.setUser_id(user);
            laptopAnnouncement.setLocation("Warsaw");
            laptopAnnouncement.setPhone_number("123000789");
            laptopAnnouncement.setPrice((float) 700.0);
            laptopAnnouncement.setDescription("Bought this laptop one year ago, still works great. 8GB RAM, 500GB SSD, i5 processor. Battery lasts for like 3 hours under big usage");
            laptopAnnouncement.setIs_hidden(false);
            laptopAnnouncement.setDatetime(new Date());
            announcementRepository.save(laptopAnnouncement);

            PrivateMessage message1 = new PrivateMessage();
            message1.setBuyer(user);
            message1.setSeller(iPhoneAnnouncement.getUser_id() == user);
            message1.setDatetime(new Date());
            message1.setMessage("Hello I am interested in buying this product");
            message1.setAnnouncement_id(iPhoneAnnouncement);
            privateMessageRepository.save(message1);

            PrivateMessage message2 = new PrivateMessage();
            message2.setBuyer(user);
            message2.setSeller(iPhoneAnnouncement2.getUser_id() == user);
            message2.setDatetime(new Date());
            message2.setMessage("Hi, I would like to buy this iPhone");
            message2.setAnnouncement_id(iPhoneAnnouncement2);
            privateMessageRepository.save(message2);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't finish an operation");
        }
        result.put("result", "success");
        return result;
    }


    @RequestMapping(value = { "/admin_auth" }, method = RequestMethod.GET)
    public List<User> adminUserList() {
        return userRepository.findAll();
    }


    @RequestMapping(value = { "/admin_auth" }, method = RequestMethod.POST)
    public User saveUser(@RequestBody User inputUser) {
        User user = userService.getLoggedInUser();
        if (user == null)
            throw new RuntimeException("Not logged in");

        inputUser.setId(null);
        inputUser.setAnnouncements(null);
        return userService.saveUser(inputUser);
    }


    @RequestMapping(value = { "/admin_auth" }, method = RequestMethod.PUT)
    public User editUser(@RequestBody User inputUser) {
        User user = userService.getLoggedInUser();
        if (user == null)
            throw new RuntimeException("Not logged in");
        return userService.saveUser(inputUser);
    }


    @RequestMapping(value = { "/admin_auth/{id}" }, method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        User user = userService.getLoggedInUser();
        if (user == null)
            throw new RuntimeException("Not logged in");
        Optional<User> userToDelete = userRepository.findById(id);
        if (userToDelete.isPresent()) {
            userToDelete.get().setRoles(Collections.emptySet());
            userRepository.save(userToDelete.get());
            userRepository.delete(userToDelete.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = { "/admin_announcement" }, method = RequestMethod.GET)
    public List<Announcement> adminAnnouncementList() {
        return announcementRepository.findAll();
    }


    @RequestMapping(value = { "/admin_announcement/hide/{announcement_id}" }, method = RequestMethod.PUT)
    public Announcement hideAnnouncement(@PathVariable("announcement_id") Integer announcement_id) {
        Optional<Announcement> announcement = announcementRepository.findById(announcement_id);
        if (announcement.isPresent()) {
            announcement.get().setIs_hidden(true);
            return announcementRepository.save(announcement.get());
        }
        return null;
    }


    @RequestMapping(value = { "/admin_announcement/delete/{announcement_id}" }, method = RequestMethod.DELETE)
    public Map<String, String> deleteAnnouncement(@PathVariable("announcement_id") Integer announcement_id) {
        Optional<Announcement> announcement = announcementRepository.findById(announcement_id);
        Map<String, String> result = new HashMap<>();
        if (announcement.isPresent()) {
            announcementRepository.delete(announcement.get());
            result.put("result", "success");
        } else {
            result.put("result", "failure");
        }
        return result;
    }

    @RequestMapping(value = { "/admin_category" }, method = RequestMethod.GET)
    public List<Category> adminCategoryList() {
        return categoryRepository.findAll();
    }

    @RequestMapping(value = {"/admin_category/create"}, method=RequestMethod.POST)
    public Category createCategory(@RequestBody Category new_category) {
        new_category.setId(null);
        return categoryRepository.save(new_category);
    }

    @RequestMapping(value = {"/admin_category/delete/{category_id}"}, method = RequestMethod.DELETE)
    public Map<String, String> deleteCategory(@PathVariable("category_id") Integer category_id) {
        Map<String, String> result = new HashMap<>();
        Optional <Category> category = categoryRepository.findById(category_id);
        if (category.isPresent()) {
            categoryRepository.delete(category.get());
            result.put("result", "success");
        } else {
            result.put("result", "failure");
        }
        return result;
    }

    @RequestMapping(value = { "/admin_priv" }, method = RequestMethod.GET)
    public List<PrivateMessage> adminPrivList() {
        return privateMessageRepository.findAll();
    }


    @RequestMapping(value="/create_admin", method=RequestMethod.POST)
    public User createAdmin(@RequestBody User user) {
        user.setId(null);
        user.setAnnouncements(null);
        return userService.saveAdminUser(user);
    }
}


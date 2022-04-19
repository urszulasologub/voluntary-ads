package com.example.announcements.controllers;


import com.example.announcements.models.*;
import com.example.announcements.repository.*;
import com.example.announcements.service.AnnouncementService;
import com.example.announcements.service.CategoryService;
import com.example.announcements.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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
    AnnouncementService announcementService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

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
            User admin = userService.seedUser("admin@example.com", "admin123", true);
            User user = userService.seedUser("user@example.com", "admin123", false);


            Category transportCategory = categoryService.seedCategory("Transport");
            Category dwellingCategory =  categoryService.seedCategory("Dwelling");

            Announcement busTransportAnnouncement = announcementService.seedAnnouncement(transportCategory,
                    "Bus Transport", admin, "Korczowa", "123456789", (float) 30,
                    "I own a bus, I offer transport from Korczowa border area to Warsaw and any city en-route",
                    false, new Date());

            Announcement packageTransportAnnouncement = announcementService.seedAnnouncement(transportCategory,
                    "Package transport", user, "Warsaw", "123000789", (float) 20,
                    "I can transport any package smaller than 1m each dimension to any of: Kiev, Lviv, Odessa",
                    false, new Date());

            Announcement hrubieszowAnnouncement = announcementService.seedAnnouncement(dwellingCategory,
                    "Free rooms", admin, "Hrubieszów", "123456789", (float) 1,
                    "I have 4 people worth of space, near Hrubieszów. Message for details.",
                    false, new Date());

            Announcement zamoscAnnouncement = announcementService.seedAnnouncement(dwellingCategory,
                    "House for rent CHEAP", user, "50.71157202495769, 23.28084820760046", "123000789",
                    (float) 100,"I have a free house in Zamość, i can rent for very cheap, the house is big" +
                            " enough for three families.",false, new Date());


            PrivateMessage message1 = new PrivateMessage();
            message1.setBuyer(user);
            message1.setSeller(busTransportAnnouncement.getUser_id() == user);
            message1.setDatetime(new Date());
            message1.setMessage("I'd like to request 3 spots");
            message1.setAnnouncement_id(busTransportAnnouncement);
            privateMessageRepository.save(message1);

            PrivateMessage message2 = new PrivateMessage();
            message2.setBuyer(user);
            message2.setSeller(packageTransportAnnouncement.getUser_id() == user);
            message2.setDatetime(new Date());
            message2.setMessage("I have a package that's 1.05m long, can i still commission you?");
            message2.setAnnouncement_id(packageTransportAnnouncement);
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
            return ResponseEntity.status(HttpStatus.OK).build();
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
        try {
            announcementRepository.delete(announcement.get());
            result.put("result", "success");
        } catch (Exception e) {
            result.put("result", "failure");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
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
        Optional<Category> category = categoryRepository.findById(category_id);
        if (categoryRepository.count() == 1) {
            result.put("result", "failure");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            categoryRepository.delete(category.get());
            result.put("result", "success");
        } catch (Exception e) {
            result.put("result", "failure");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
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


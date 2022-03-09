package com.example.announcements.controllers;

import com.example.announcements.models.Role;
import com.example.announcements.models.User;
import com.example.announcements.repository.RoleRepository;
import com.example.announcements.service.JwtService;
import com.example.announcements.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
public class RestAuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public Map<String, String> login(@RequestBody User user, HttpServletRequest httpServletRequest) {
        Map<String, String> result = new HashMap<>();

        if (user.getEmail() != null && user.getPassword() != null) {
            String email = user.getEmail();
            String password = user.getPassword();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);

            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User loggedInUser = userService.getLoggedInUser();

            result.put("result", "success");
            result.put("session", jwtService.generateJwtToken(loggedInUser.getId(), new Date(System.currentTimeMillis() + 24*3600*1000)));
            Boolean is_admin = false;
            for (Role role : loggedInUser.getRoles())
                if (role.getRole().toString().equals("ADMIN_USER"))
                    is_admin = true;
            result.put("is_admin", is_admin.toString());
            result.put("id", loggedInUser.getId().toString());
        } else {
            result.put("result", "failure");
        }
        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpSession session) {
        session.invalidate();
    }


    @RequestMapping(value="/register", method=RequestMethod.POST)
    public User registerUser(@RequestBody User user) {
        user.setId(null);
        user.setAnnouncements(null);
        return userService.saveUser(user);
    }


    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home"); // resources/template/home.html
        return modelAndView;
    }


    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView adminHome() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin"); // resources/template/admin.html
        return modelAndView;
    }

}

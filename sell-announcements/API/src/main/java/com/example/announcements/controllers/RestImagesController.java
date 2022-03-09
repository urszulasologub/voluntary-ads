package com.example.announcements.controllers;

import com.example.announcements.models.Announcement;
import com.example.announcements.repository.AnnouncementRepository;
import com.example.announcements.service.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@SpringBootApplication
@RestController
public class RestImagesController  {

	@Autowired
	AnnouncementRepository announcementRepository;

	@Autowired
	UserService userService;

	@RequestMapping(value="announcements/upload/{announcement_id}", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Announcement uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("announcement_id") Integer announcement_id) throws IOException {
		Optional<Announcement> announcement = announcementRepository.findById(announcement_id);
		if (announcement.isPresent()) {
			if (userService.getLoggedInUser() != announcement.get().getUser_id())
				throw new RuntimeException("Cannot modify someone's announcement");
			//File convertFile = new File("src/main/resources/images/" + file.getOriginalFilename());
			//convertFile.createNewFile();
			//FileOutputStream fout = new FileOutputStream(convertFile);
			Byte[] bytes = new Byte[file.getBytes().length];
			int i = 0;
			for (byte b : file.getBytes())
				bytes[i++] = b;
			announcement.get().setImage(bytes);
			//fout.write(file.getBytes());
			//fout.close();
			return announcement.get();
		}
		throw new RuntimeException("Announcement not found");
	}
}
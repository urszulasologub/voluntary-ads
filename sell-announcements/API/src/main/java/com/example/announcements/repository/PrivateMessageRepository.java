package com.example.announcements.repository;


import com.example.announcements.models.Announcement;
import com.example.announcements.models.PrivateMessage;
import com.example.announcements.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Integer> {
	public List<PrivateMessage> findByAnnouncementId(Announcement announcement_id);
}

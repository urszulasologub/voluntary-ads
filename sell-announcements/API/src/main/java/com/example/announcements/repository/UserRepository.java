package com.example.announcements.repository;

import com.example.announcements.models.Announcement;
import com.example.announcements.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findUserByEmail(String email);
}

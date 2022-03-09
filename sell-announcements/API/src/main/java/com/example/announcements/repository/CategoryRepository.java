package com.example.announcements.repository;


import com.example.announcements.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	public Optional<Category> findById(Integer Id);
	public Category findCategoryById(Integer Id);
}

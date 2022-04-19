package com.example.announcements.service;

import com.example.announcements.models.Category;
import com.example.announcements.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryServiceImp implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category seedCategory(String name) {
        Category category = new Category();
        category.setName(name);

        return saveCategory(category);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
}

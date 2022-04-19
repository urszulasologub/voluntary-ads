package com.example.announcements.service;

import com.example.announcements.models.Category;
import com.example.announcements.models.User;

public interface CategoryService {
    Category seedCategory(String name);

    Category saveCategory(Category category);

}

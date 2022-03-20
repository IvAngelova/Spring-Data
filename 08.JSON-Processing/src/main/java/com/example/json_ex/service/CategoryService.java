package com.example.json_ex.service;

import com.example.json_ex.model.dto.CategoryStatsDto;
import com.example.json_ex.model.entity.Category;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> getRandomCategories();

    List<CategoryStatsDto> getAllCategoriesByProductsCount();


}

package com.example.xml_ex.service;

import com.example.xml_ex.model.dto.CategorySeedDto;
import com.example.xml_ex.model.dto.ex3.CategoryViewRootDto;
import com.example.xml_ex.model.entity.Category;

import java.util.Set;

public interface CategoryService {
    void seedCategories(Set<CategorySeedDto> categories);

    long getEntityCount();

    Set<Category> getRandomCategories();

    CategoryViewRootDto findAllCategoriesInfo();
}

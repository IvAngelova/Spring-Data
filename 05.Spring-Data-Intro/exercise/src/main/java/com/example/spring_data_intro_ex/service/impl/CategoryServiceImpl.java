package com.example.spring_data_intro_ex.service.impl;

import com.example.spring_data_intro_ex.model.entity.Category;
import com.example.spring_data_intro_ex.repository.CategoryRepository;
import com.example.spring_data_intro_ex.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORIES_FILE_PATH = "src/main/resources/files/categories.txt";

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void seedCategories() throws IOException {
        if (this.categoryRepository.count() > 0) {
            return;
        }
        Files
                .readAllLines(Path.of(CATEGORIES_FILE_PATH))
                .stream()
                .filter(row -> !row.isEmpty())
                .forEach(categoryName -> {
                    Category category = new Category(categoryName);
                    this.categoryRepository.save(category);
                });

    }

    @Override
    public Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();
        int rdmInt = ThreadLocalRandom.current().nextInt(1, 4);
        long categoryRepoCount = this.categoryRepository.count();

        for (int i = 0; i < rdmInt; i++) {
            long randomCategoryId = ThreadLocalRandom.current()
                    .nextLong(1, categoryRepoCount + 1);

            Category category = this.categoryRepository
                    .findById(randomCategoryId)
                    .orElse(null);

            categories.add(category);
        }

        return categories;
    }
}

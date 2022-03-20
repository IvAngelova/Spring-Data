package com.example.json_ex.service.impl;

import com.example.json_ex.model.dto.CategorySeedDto;
import com.example.json_ex.model.dto.CategoryStatsDto;
import com.example.json_ex.model.entity.Category;
import com.example.json_ex.repository.CategoryRepository;
import com.example.json_ex.service.CategoryService;
import com.example.json_ex.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.json_ex.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORIES_FILE_NAME = "categories.json";

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public void seedCategories() throws IOException {

        if (categoryRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + CATEGORIES_FILE_NAME));

        CategorySeedDto[] categorySeedDtos = gson.fromJson(fileContent, CategorySeedDto[].class);

        Arrays.stream(categorySeedDtos)
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepository::save);
    }

    @Override
    public Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();
        int randomInt = ThreadLocalRandom.current().nextInt(1, 3);
        long totalCountCategories = categoryRepository.count();

        for (int i = 0; i <= randomInt; i++) {
            long randomId = ThreadLocalRandom
                    .current()
                    .nextLong(1, totalCountCategories + 1);
            Category category = categoryRepository
                    .findById(randomId)
                    .orElse(null);
            categories.add(category);
        }
        return categories;
    }

    @Override
    public List<CategoryStatsDto> getAllCategoriesByProductsCount() {
        return categoryRepository
                .getCategoryStats();
    }

}

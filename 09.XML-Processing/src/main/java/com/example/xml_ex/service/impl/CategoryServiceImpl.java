package com.example.xml_ex.service.impl;

import com.example.xml_ex.model.dto.CategorySeedDto;
import com.example.xml_ex.model.dto.ex3.CategoryViewDto;
import com.example.xml_ex.model.dto.ex3.CategoryViewRootDto;
import com.example.xml_ex.model.entity.Category;
import com.example.xml_ex.model.entity.Product;
import com.example.xml_ex.repository.CategoryRepository;
import com.example.xml_ex.service.CategoryService;
import com.example.xml_ex.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(ValidationUtil validationUtil, ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void seedCategories(Set<CategorySeedDto> categories) {
        categories.
                stream()
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepository::save);

    }

    @Override
    public long getEntityCount() {
        return categoryRepository.count();
    }

    @Override
    public Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();
        long categoriesCount = categoryRepository.count();

        for (int i = 0; i < 2; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1, categoriesCount + 1);
            Category category = categoryRepository.findById(randomId).orElse(null);
            categories.add(category);
        }
        return categories;
    }

    @Override
    public CategoryViewRootDto findAllCategoriesInfo() {
        CategoryViewRootDto categoryViewRootDto = new CategoryViewRootDto();
        List<CategoryViewDto> categoryViewDtos = this.categoryRepository
                .findAllCategoriesByCountOfProducts()
                .stream()
                .map(category -> {
                    CategoryViewDto categoryViewDto = modelMapper.map(category, CategoryViewDto.class);
                    int countProducts = category.getProducts().size();
                    categoryViewDto.setProductsCount(countProducts);
                    BigDecimal sum = category.getProducts()
                            .stream()
                            .map(Product::getPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    categoryViewDto.setTotalRevenue(sum);
                    BigDecimal avgPrice = sum.divide(BigDecimal.valueOf(countProducts), 6, RoundingMode.CEILING);
                    categoryViewDto.setAvgPrice(avgPrice);
                    return categoryViewDto;
                })
                .collect(Collectors.toList());

        categoryViewRootDto.setCategories(categoryViewDtos);
        return categoryViewRootDto;
    }
}

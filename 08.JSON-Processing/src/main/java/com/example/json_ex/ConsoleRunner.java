package com.example.json_ex;

import com.example.json_ex.model.dto.*;
import com.example.json_ex.model.dto.ex4.UserCountDto;
import com.example.json_ex.model.dto.ex4.UserFirstLastAgeSoldProductsDto;
import com.example.json_ex.service.CategoryService;
import com.example.json_ex.service.ProductService;
import com.example.json_ex.service.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private static final String OUTPUT_PATH = "src/main/resources/files/output/";
    private static final String PRODUCTS_IN_RANGE_FILE_NAME = "products_in_range.json";
    private static final String USERS_SOLD_PRODUCTS = "users_sold_products.json";
    private static final String CATEGORIES_BY_PRODUCTS = "categories_by_products.json";
    private static final String USERS_AND_PRODUCTS = "users_and_products.json";

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final Scanner scanner;
    private final Gson gson;

    public ConsoleRunner(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

        System.out.println("Please, enter exercise number: ");
        int exerciseNumber = Integer.parseInt(scanner.nextLine());

        switch (exerciseNumber) {
            case 1 -> productsInRange();
            case 2 -> soldProducts();
            case 3 -> categoriesByProductsCount();
            case 4 -> usersAndProducts();
        }

    }

    private void usersAndProducts() throws IOException {
        UserCountDto userCountDto = this.userService
                .getUsersCountWithMoreThanOneSoldProduct();

        String content = gson.toJson(userCountDto);

        writeToFile(OUTPUT_PATH + USERS_AND_PRODUCTS, content);

    }

    private void categoriesByProductsCount() throws IOException {

        List<CategoryStatsDto> categoryStatsDtos = this.categoryService
                .getAllCategoriesByProductsCount();

        String content = gson.toJson(categoryStatsDtos);

        writeToFile(OUTPUT_PATH + CATEGORIES_BY_PRODUCTS, content);
    }

    private void soldProducts() throws IOException {
        List<UserSoldDto> userSoldDtos = this.userService
                .getAllUsersWithAtLeastOneItemSoldWithBuyer();

        String content = gson.toJson(userSoldDtos);

        writeToFile(OUTPUT_PATH + USERS_SOLD_PRODUCTS, content);


    }

    private void productsInRange() throws IOException {
        List<ProductNameAndPriceDto> productDtos = this.productService
                .getAllProductsInRangeOrderByPrice(BigDecimal.valueOf(500), BigDecimal.valueOf(1000));

        String content = gson.toJson(productDtos);

        writeToFile(OUTPUT_PATH + PRODUCTS_IN_RANGE_FILE_NAME, content);

    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files
                .write(Path.of(filePath), Collections.singleton(content));
    }

    private void seedData() throws IOException {
        this.categoryService.seedCategories();
        this.userService.seedUsers();
        this.productService.seedProducts();
    }
}

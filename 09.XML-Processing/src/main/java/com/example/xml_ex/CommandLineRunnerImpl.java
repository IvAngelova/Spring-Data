package com.example.xml_ex;

import com.example.xml_ex.model.dto.CategorySeedRootDto;
import com.example.xml_ex.model.dto.ProductSeedRootDto;
import com.example.xml_ex.model.dto.UserSeedRootDto;
import com.example.xml_ex.model.dto.ex1.ProductViewRootDto;
import com.example.xml_ex.model.dto.ex2.UserSoldRootDto;
import com.example.xml_ex.model.dto.ex3.CategoryViewRootDto;
import com.example.xml_ex.service.CategoryService;
import com.example.xml_ex.service.ProductService;
import com.example.xml_ex.service.UserService;
import com.example.xml_ex.util.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private static final String RESOURCES_FILE_PATH = "src/main/resources/files/";
    private static final String CATEGORIES_FILE_NAME = "categories.xml";
    private static final String USERS_FILE_NAME = "users.xml";
    private static final String PRODUCTS_FILE_NAME = "products.xml";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/files/output/";
    private static final String PRODUCTS_IN_RANGE_FILE_NAME = "products-in-range.xml";
    private static final String USERS_SOLD_PRODUCTS_FILE_NAME = "users-sold-products.xml";
    private static final String CATEGORIES_BY_PRODUCTS_FILE_NAME = "categories-by-products.xml";


    private final CategoryService categoryService;
    private final XmlParser xmlParser;
    private final UserService userService;
    private final ProductService productService;
    private final Scanner scanner;

    public CommandLineRunnerImpl(CategoryService categoryService, XmlParser xmlParser, UserService userService, ProductService productService) {
        this.categoryService = categoryService;
        this.xmlParser = xmlParser;
        this.userService = userService;
        this.productService = productService;
        scanner = new Scanner(System.in);
    }


    @Override
    public void run(String... args) throws Exception {
        seedData();

        System.out.println("Please, enter exercise number:");
        int exNum = Integer.parseInt(scanner.nextLine());

        switch (exNum) {
            case 1 -> productsInRange();
            case 2 -> soldProducts();
            case 3 -> categoriesByProductsCount();
        }


    }

    private void categoriesByProductsCount() throws JAXBException {
        CategoryViewRootDto categoryViewRootDto = this.categoryService.findAllCategoriesInfo();

        xmlParser.writeToFile(OUTPUT_FILE_PATH + CATEGORIES_BY_PRODUCTS_FILE_NAME, categoryViewRootDto);

    }

    private void soldProducts() throws JAXBException {
        UserSoldRootDto userSoldRootDto = this.userService.findAllUsersWithMinOneItemSold();

        xmlParser.writeToFile(OUTPUT_FILE_PATH + USERS_SOLD_PRODUCTS_FILE_NAME, userSoldRootDto);
    }

    private void productsInRange() throws JAXBException {
        ProductViewRootDto productViewRootDto = productService.findAllProductsInRangeWithoutBuyer();

        xmlParser.writeToFile(OUTPUT_FILE_PATH + PRODUCTS_IN_RANGE_FILE_NAME, productViewRootDto);
    }

    private void seedData() throws JAXBException, FileNotFoundException {
        if (categoryService.getEntityCount() == 0) {
            CategorySeedRootDto categorySeedRootDto = xmlParser
                    .fromFile(RESOURCES_FILE_PATH + CATEGORIES_FILE_NAME, CategorySeedRootDto.class);

            categoryService.seedCategories(categorySeedRootDto.getCategories());
        }

        if (userService.getEntityCount() == 0) {
            UserSeedRootDto userSeedRootDto = xmlParser
                    .fromFile(RESOURCES_FILE_PATH + USERS_FILE_NAME, UserSeedRootDto.class);

            userService.seedUsers(userSeedRootDto.getUsers());
        }

        if (productService.getEntityCount() == 0) {
            ProductSeedRootDto productSeedRootDto = xmlParser
                    .fromFile(RESOURCES_FILE_PATH + PRODUCTS_FILE_NAME, ProductSeedRootDto.class);

            productService.seedProducts(productSeedRootDto.getProducts());
        }
    }
}

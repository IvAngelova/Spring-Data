package com.example.json_ex.service.impl;

import com.example.json_ex.model.dto.ProductNameAndPriceDto;
import com.example.json_ex.model.dto.ProductSeedDto;
import com.example.json_ex.model.entity.Product;
import com.example.json_ex.model.entity.User;
import com.example.json_ex.repository.ProductRepository;
import com.example.json_ex.service.CategoryService;
import com.example.json_ex.service.ProductService;
import com.example.json_ex.service.UserService;
import com.example.json_ex.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.json_ex.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {
    private static final String PRODUCTS_FILE_NAME = "products.json";

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedProducts() throws IOException {
        if (productRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + PRODUCTS_FILE_NAME));

        ProductSeedDto[] productSeedDtos = gson.fromJson(fileContent, ProductSeedDto[].class);

        Arrays.stream(productSeedDtos)
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {

                    Product product = modelMapper.map(productSeedDto, Product.class);

                    User buyer = null;
                    if (product.getPrice().compareTo(new BigDecimal(845)) > 0) {
                        buyer = userService.getRandomUser();
                    }
                    User seller = userService.getRandomUser();
                    if (buyer != null) {
                        ensureBuyerAndSellerHaveDifferentIds(buyer, seller);
                    }

                    product.setBuyer(buyer);
                    product.setSeller(seller);

                    product.setCategories(categoryService.getRandomCategories());

                    return product;
                })
                .forEach(productRepository::save);
    }

    @Override
    public List<ProductNameAndPriceDto> getAllProductsInRangeOrderByPrice(BigDecimal lower, BigDecimal upper) {
        return this.productRepository.findAllByPriceBetweenAndBuyerIsNullOrderByPriceAsc(lower,upper)
                .stream()
                .map(product -> {
                    ProductNameAndPriceDto productNameAndPriceDto = modelMapper.map(product, ProductNameAndPriceDto.class);

                    productNameAndPriceDto.setSeller(String.format("%s %s",
                                    product.getSeller().getFirstName(),
                                    product.getSeller().getLastName()));

                    return productNameAndPriceDto;
                })
                .collect(Collectors.toList());
    }

    private void ensureBuyerAndSellerHaveDifferentIds(User buyer, User seller) {
        while (Objects.equals(buyer.getId(), seller.getId())) {
            buyer = userService.getRandomUser();
        }
    }
}

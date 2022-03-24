package com.example.xml_ex.service.impl;

import com.example.xml_ex.model.dto.ProductSeedDto;
import com.example.xml_ex.model.dto.ex1.ProductViewDto;
import com.example.xml_ex.model.dto.ex1.ProductViewRootDto;
import com.example.xml_ex.model.entity.Product;
import com.example.xml_ex.repository.ProductRepository;
import com.example.xml_ex.service.CategoryService;
import com.example.xml_ex.service.ProductService;
import com.example.xml_ex.service.UserService;
import com.example.xml_ex.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, ProductRepository productRepository, UserService userService, CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public long getEntityCount() {
        return productRepository.count();
    }

    @Override
    public void seedProducts(List<ProductSeedDto> products) {
        products.
                stream()
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    product.setSeller(userService.getRandomUser());
                    if (product.getPrice().compareTo(BigDecimal.valueOf(700L)) > 0) {
                        product.setBuyer(userService.getRandomUser());
                    }

                    product.setCategories(categoryService.getRandomCategories());

                    return product;

                })
                .forEach(productRepository::save);
    }

    @Override
    public ProductViewRootDto findAllProductsInRangeWithoutBuyer() {
        ProductViewRootDto productViewRootDto = new ProductViewRootDto();

        List<ProductViewDto> productViewDtoList = this.productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPriceAsc(BigDecimal.valueOf(500), BigDecimal.valueOf(1000))
                .stream()
                .map(product -> {
                    ProductViewDto productViewDto = modelMapper.map(product, ProductViewDto.class);
                    productViewDto.setSeller(String.format("%s %s",
                            product.getSeller().getFirstName() == null
                                    ? ""
                                    : product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));
                    return productViewDto;
                })
                .collect(Collectors.toList());

        productViewRootDto.setProducts(productViewDtoList);

        return productViewRootDto;
    }
}

package com.example.xml_ex.service;

import com.example.xml_ex.model.dto.ProductSeedDto;
import com.example.xml_ex.model.dto.ex1.ProductViewRootDto;

import java.util.List;

public interface ProductService {
    long getEntityCount();

    void seedProducts(List<ProductSeedDto> products);

    ProductViewRootDto findAllProductsInRangeWithoutBuyer();
}

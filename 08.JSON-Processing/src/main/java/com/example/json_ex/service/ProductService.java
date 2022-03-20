package com.example.json_ex.service;

import com.example.json_ex.model.dto.ProductNameAndPriceDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void seedProducts() throws IOException;

    List<ProductNameAndPriceDto> getAllProductsInRangeOrderByPrice(BigDecimal lower, BigDecimal upper);
}

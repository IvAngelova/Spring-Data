package com.example.json_ex.model.dto.ex4;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class ProductCountDto {
    @Expose
    private long count;
    @Expose
    private Set<ProductDetailsDto> products;

    public ProductCountDto() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Set<ProductDetailsDto> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDetailsDto> products) {
        this.products = products;
    }
}

package com.example.json_ex.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CategoryStatsDto {
    @Expose
    @SerializedName("category")
    private String name;
    @Expose
    private long productsCount;
    @Expose
    private Double averagePrice;
    @Expose
    private BigDecimal totalRevenue;

    public CategoryStatsDto() {
    }

    public CategoryStatsDto(String name, long productsCount, Double averagePrice, BigDecimal totalRevenue) {
        this.name = name;
        this.productsCount = productsCount;
        this.averagePrice = averagePrice;
        this.totalRevenue = totalRevenue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(long productsCount) {
        this.productsCount = productsCount;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}

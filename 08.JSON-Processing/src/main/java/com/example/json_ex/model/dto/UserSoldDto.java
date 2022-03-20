package com.example.json_ex.model.dto;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class UserSoldDto {
    @Expose
    private String firstName;
    @Expose
    private String lastName;

    @Expose
    private Set<ProductSoldDto> soldProducts;

    public UserSoldDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<ProductSoldDto> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(Set<ProductSoldDto> soldProducts) {
        this.soldProducts = soldProducts;
    }
}

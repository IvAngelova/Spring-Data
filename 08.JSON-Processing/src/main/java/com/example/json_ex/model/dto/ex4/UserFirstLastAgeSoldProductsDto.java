package com.example.json_ex.model.dto.ex4;

import com.google.gson.annotations.Expose;

public class UserFirstLastAgeSoldProductsDto {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private Integer age;
    @Expose
    private ProductCountDto soldProducts;

    public UserFirstLastAgeSoldProductsDto() {
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public ProductCountDto getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(ProductCountDto soldProducts) {
        this.soldProducts = soldProducts;
    }
}

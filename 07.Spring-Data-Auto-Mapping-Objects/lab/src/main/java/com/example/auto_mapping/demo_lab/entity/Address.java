package com.example.auto_mapping.demo_lab.entity;

public class Address {
    private Long id;
    private String street;
    private int streetNumber;
    private String city;
    private String country;

    public Address(String street, int streetNumber, String city, String country) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.country = country;
    }

    public Address() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

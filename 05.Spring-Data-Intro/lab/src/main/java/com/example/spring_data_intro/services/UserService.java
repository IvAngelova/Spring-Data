package com.example.spring_data_intro.services;

import com.example.spring_data_intro.exceptions.UserNotFoundException;

import java.math.BigDecimal;

public interface UserService {
    void registerUser(String username, Integer age, BigDecimal initialAmount);
    void addAccount(BigDecimal amount, String username) throws UserNotFoundException;
}

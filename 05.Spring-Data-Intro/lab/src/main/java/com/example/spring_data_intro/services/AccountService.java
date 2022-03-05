package com.example.spring_data_intro.services;

import com.example.spring_data_intro.exceptions.InsufficientFundsException;

import java.math.BigDecimal;

public interface AccountService {
    void withdrawMoney(BigDecimal amount, Long id) throws InsufficientFundsException;
    void depositMoney(BigDecimal amount, Long id);
}

package com.example.spring_data_intro.services;

import com.example.spring_data_intro.exceptions.InsufficientFundsException;
import com.example.spring_data_intro.models.Account;
import com.example.spring_data_intro.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void withdrawMoney(BigDecimal amount, Long id) throws InsufficientFundsException {
        Account accountById = this.accountRepository.findAccountById(id).orElseThrow();

        if (accountById.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        accountById.setBalance(accountById.getBalance().subtract(amount));
        this.accountRepository.save(accountById);

    }

    @Override
    public void depositMoney(BigDecimal amount, Long id) {
        Account accountById = this.accountRepository.findAccountById(id).orElseThrow();
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            accountById.setBalance(accountById.getBalance().add(amount));
            this.accountRepository.save(accountById);
        }
    }
}

package com.example.spring_data_intro.services;

import com.example.spring_data_intro.exceptions.UserNotFoundException;
import com.example.spring_data_intro.models.Account;
import com.example.spring_data_intro.models.User;
import com.example.spring_data_intro.repositories.AccountRepository;
import com.example.spring_data_intro.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private AccountRepository accountRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;

    }

    @Override
    public void registerUser(String username, Integer age, BigDecimal initialAmount) {
        Optional<User> found = this.userRepository.getByUsername(username);

        if (found.isEmpty()) {
            User user = new User(username,age);
            this.userRepository.save(user);

            Account firstAccount = new Account(initialAmount, user);

            this.accountRepository.save(firstAccount);

        }

    }

    @Override
    public void addAccount(BigDecimal amount, String username) throws UserNotFoundException {
        User user = this.userRepository.getByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Account account = new Account(amount, user);
        this.accountRepository.save(account);
    }
}

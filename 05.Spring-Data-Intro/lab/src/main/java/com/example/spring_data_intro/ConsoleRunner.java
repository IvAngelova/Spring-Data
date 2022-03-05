package com.example.spring_data_intro;

import com.example.spring_data_intro.exceptions.InsufficientFundsException;
import com.example.spring_data_intro.exceptions.UserNotFoundException;
import com.example.spring_data_intro.services.AccountService;
import com.example.spring_data_intro.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private AccountService accountService;
    private UserService userService;

    @Autowired
    public ConsoleRunner(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        this.userService.registerUser("Pesho", 20, new BigDecimal(25000) );
        try {
            this.userService.addAccount(new BigDecimal(5000), "Pesho");
        } catch (UserNotFoundException e) {
            System.out.println(e.getClass().getSimpleName());
        }

        try {
            this.accountService.withdrawMoney(new BigDecimal(20000), 1L);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getClass().getSimpleName());
        }

        this.accountService.depositMoney(new BigDecimal(30000), 2L);


    }
}

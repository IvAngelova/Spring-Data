package com.example.game_store.service.impl;

import com.example.game_store.model.dto.UserLoginDto;
import com.example.game_store.model.dto.UserRegisterDto;
import com.example.game_store.model.entity.User;
import com.example.game_store.repository.UserRepository;
import com.example.game_store.service.UserService;
import com.example.game_store.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private User loggedInUser;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {

        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            System.out.println("Wrong confirm password");
            return;
        }

        Set<ConstraintViolation<UserRegisterDto>> violations = validationUtil.violations(userRegisterDto);

        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }

        User userToRegister = modelMapper.map(userRegisterDto, User.class);

        long userCount = this.userRepository.count();

        if (userCount == 0) {
            userToRegister.setAdmin(true);
        }

        userRepository.save(userToRegister);

        System.out.println(userToRegister.getFullName() + " was registered");

    }

    @Override
    public void loginUser(UserLoginDto userLoginDto) {

        if (hasLoggedInUser()) {
            System.out.println("There is already logged in user!");
            return;
        }

        Set<ConstraintViolation<UserLoginDto>> violations = validationUtil.violations(userLoginDto);

        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }

        User user = userRepository
                .findUserByPasswordAndEmail(userLoginDto.getPassword(), userLoginDto.getEmail()).orElse(null);

        if (user == null) {
            System.out.println("Incorrect username / password");
            return;
        }

        loggedInUser = user;
        System.out.println("Successfully logged in " + user.getFullName());

    }

    @Override
    public void logout() {
        if (loggedInUser == null) {
            System.out.println("Cannot log out. No user was logged in.");
        } else {
            System.out.printf("User %s successfully logged out%n", loggedInUser.getFullName());
            loggedInUser = null;
        }
    }

    @Override
    public boolean hasLoggedInUser() {
        return this.loggedInUser != null;
    }

    @Override
    public boolean isUserAdmin() {
        return this.loggedInUser.getAdmin();
    }

    @Override
    public void getCurrentUserGames() {
        if (!hasLoggedInUser()) {
            System.out.println("No logged in user");
            return;
        }

        this.loggedInUser.getGames().forEach(g -> System.out.println(g.getTitle()));

    }
}

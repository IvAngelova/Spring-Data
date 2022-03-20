package com.example.json_ex.service;

import com.example.json_ex.model.dto.ex4.UserCountDto;
import com.example.json_ex.model.dto.ex4.UserFirstLastAgeSoldProductsDto;
import com.example.json_ex.model.dto.UserSoldDto;
import com.example.json_ex.model.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void seedUsers() throws IOException;

    User getRandomUser();

    List<UserSoldDto> getAllUsersWithAtLeastOneItemSoldWithBuyer();

    UserCountDto getUsersCountWithMoreThanOneSoldProduct();
}

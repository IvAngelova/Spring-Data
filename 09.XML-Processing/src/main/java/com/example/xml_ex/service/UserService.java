package com.example.xml_ex.service;

import com.example.xml_ex.model.dto.UserSeedDto;
import com.example.xml_ex.model.dto.ex2.UserSoldRootDto;
import com.example.xml_ex.model.entity.User;

import java.util.List;

public interface UserService {
    long getEntityCount();

    void seedUsers(List<UserSeedDto> users);

    User getRandomUser();

    UserSoldRootDto findAllUsersWithMinOneItemSold();
}

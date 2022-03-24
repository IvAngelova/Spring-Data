package com.example.xml_ex.service.impl;

import com.example.xml_ex.model.dto.UserSeedDto;
import com.example.xml_ex.model.dto.ex2.UserSoldDto;
import com.example.xml_ex.model.dto.ex2.UserSoldRootDto;
import com.example.xml_ex.model.entity.User;
import com.example.xml_ex.repository.UserRepository;
import com.example.xml_ex.service.UserService;
import com.example.xml_ex.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public long getEntityCount() {
        return userRepository.count();
    }

    @Override
    public void seedUsers(List<UserSeedDto> users) {
        users
                .stream()
                .filter(validationUtil::isValid)
                .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                .forEach(userRepository::save);
    }

    @Override
    public User getRandomUser() {
        long randomId = ThreadLocalRandom
                .current().nextLong(1, userRepository.count() + 1);
        return userRepository.findById(randomId).orElse(null);
    }

    @Override
    public UserSoldRootDto findAllUsersWithMinOneItemSold() {
        UserSoldRootDto userSoldRootDto = new UserSoldRootDto();
        List<UserSoldDto> userSoldDtos = this.userRepository.findAllUsersWithOneOrMoreSoldProductsOrderByLastNameThenFirstName()
                .stream()
                .map(user -> {
                    UserSoldDto userSoldDto = modelMapper.map(user, UserSoldDto.class);
                    return userSoldDto;
                })
                .collect(Collectors.toList());
        userSoldRootDto.setUsers(userSoldDtos);
        return userSoldRootDto;
    }
}

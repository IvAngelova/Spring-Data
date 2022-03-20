package com.example.json_ex.service.impl;

import com.example.json_ex.model.dto.ex4.UserCountDto;
import com.example.json_ex.model.dto.ex4.UserFirstLastAgeSoldProductsDto;
import com.example.json_ex.model.dto.UserSeedDto;
import com.example.json_ex.model.dto.UserSoldDto;
import com.example.json_ex.model.entity.User;
import com.example.json_ex.repository.UserRepository;
import com.example.json_ex.service.UserService;
import com.example.json_ex.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.json_ex.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class UserServiceImpl implements UserService {

    private static final String USERS_FILE_NAME = "users.json";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public void seedUsers() throws IOException {
        if (userRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + USERS_FILE_NAME));

        UserSeedDto[] userSeedDtos = gson.fromJson(fileContent, UserSeedDto[].class);

        Arrays.stream(userSeedDtos)
                .filter(validationUtil::isValid)
                .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                .forEach(userRepository::save);
    }

    @Override
    public User getRandomUser() {
        long randomId = ThreadLocalRandom.current()
                .nextLong(1, userRepository.count() + 1);
        return userRepository
                .findById(randomId)
                .orElse(null);
    }

    @Override
    public List<UserSoldDto> getAllUsersWithAtLeastOneItemSoldWithBuyer() {
        return userRepository.findAllUsersWithAtLeastOneItemSoldWithBuyerOrderByLastNameFirstName()
                .stream()
                .map(user -> modelMapper.map(user, UserSoldDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserCountDto getUsersCountWithMoreThanOneSoldProduct() {

        List<UserFirstLastAgeSoldProductsDto> userFirstLastAgeSoldProductsDtos = userRepository.findAllUsersWithMoreThanOneSoldProductOrderBySoldProductsDescLastNameAsc()
                .stream()
                .map(user -> modelMapper
                        .map(user, UserFirstLastAgeSoldProductsDto.class))
                .collect(Collectors.toList());

        UserCountDto userCountDto = new UserCountDto(userFirstLastAgeSoldProductsDtos);

        return userCountDto;

    }
}

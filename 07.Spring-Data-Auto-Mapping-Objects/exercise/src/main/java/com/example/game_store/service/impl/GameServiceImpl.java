package com.example.game_store.service.impl;

import com.example.game_store.model.dto.GameAddDto;
import com.example.game_store.model.dto.GameDetailViewDto;
import com.example.game_store.model.dto.GameViewDto;
import com.example.game_store.model.entity.Game;
import com.example.game_store.repository.GameRepository;
import com.example.game_store.service.GameService;
import com.example.game_store.service.UserService;
import com.example.game_store.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserService userService;

    public GameServiceImpl(GameRepository gameRepository, ModelMapper modelMapper, ValidationUtil validationUtil, UserService userService) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
    }

    @Override
    public void addGame(GameAddDto gameAddDto) {

        if (userNotAdmin()) {
            return;
        }

        Set<ConstraintViolation<GameAddDto>> violations = validationUtil.violations(gameAddDto);

        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }

        Game game = modelMapper.map(gameAddDto, Game.class);

        gameRepository.save(game);
        System.out.println("Added " + gameAddDto.getTitle());

    }

    @Override
    public void editGame(Long gameId, String[] values) {

        if (userNotAdmin()) {
            return;
        }

        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            System.out.println("There is no game with the given id in the catalog");
            return;
        }

        updateGameProperties(values, game);

        GameAddDto gameEditDto = modelMapper.map(game, GameAddDto.class);

        Set<ConstraintViolation<GameAddDto>> violations = validationUtil.violations(gameEditDto);
        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }

        gameRepository.save(game);
        System.out.println("Edited " + game.getTitle());


    }

    @Override
    public void deleteGame(Long gameId) {

        if (userNotAdmin()) {
            return;
        }

        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            System.out.println("There is no game with the given id in the catalog");
            return;
        }

        gameRepository.delete(game);
        System.out.println("Deleted " + game.getTitle());
    }

    @Override
    public void getAllGamesInfo() {
        gameRepository
                .findAll()
                .stream()
                .map(g -> modelMapper.map(g, GameViewDto.class))
                .forEach(System.out::println);
    }

    @Override
    public void getGameInfo(String title) {
        Game game = this.gameRepository.findGameByTitle(title).orElse(null);

        if (game == null) {
            System.out.println("There is no game with the given title");
            return;
        }

        GameDetailViewDto gameDetailViewDto = modelMapper.map(game, GameDetailViewDto.class);
        System.out.println(gameDetailViewDto.toString());
    }

    private void updateGameProperties(String[] values, Game game) {
        for (String value : values) {
            String[] arguments = value.split("=");
            String fieldName = arguments[0];
            String fieldValue = arguments[1];
            setPropertiesToGame(fieldName, fieldValue, game);
        }
    }

    private void setPropertiesToGame(String fieldName, String fieldValue, Game game) {
        switch (fieldName) {
            case "title" -> game.setTitle(fieldValue);
            case "price" -> game.setPrice(new BigDecimal(fieldValue));
            case "size" -> game.setSize(Double.parseDouble(fieldValue));
            case "trailer" -> game.setTrailer(fieldValue);
            case "thumbnailURL" -> game.setThumbnailURL(fieldValue);
            case "description" -> game.setDescription(fieldValue);
            case "releaseDate" -> game.setReleaseDate(LocalDate.parse(fieldValue, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
    }

    private boolean userNotAdmin() {
        if (!userService.hasLoggedInUser()) {
            System.out.println("User not logged in");
            return true;
        }

        if (userService.hasLoggedInUser() && !userService.isUserAdmin()) {
            System.out.println("Logged in user is not an Admin");
            return true;
        }
        return false;
    }

}

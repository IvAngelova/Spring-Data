package com.example.game_store;

import com.example.game_store.model.dto.GameAddDto;
import com.example.game_store.model.dto.UserLoginDto;
import com.example.game_store.model.dto.UserRegisterDto;
import com.example.game_store.service.GameService;
import com.example.game_store.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final Scanner scanner;
    private final UserService userService;
    private final GameService gameService;

    public CommandLineRunnerImpl(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) {
        while (true) {
            System.out.println("Please, enter your commands: ");
            String[] commands = scanner.nextLine().split("\\|");

            if (commands[0].equals("End")) {
                break;
            }

            switch (commands[0]) {
                case "RegisterUser" -> userService
                        .registerUser(new UserRegisterDto(commands[1], commands[2], commands[3], commands[4]));
                case "LoginUser" -> userService.loginUser(new UserLoginDto(commands[1], commands[2]));
                case "Logout" -> userService.logout();
                case "AddGame" -> gameService.addGame(new GameAddDto(commands[1], new BigDecimal(commands[2]), Double.parseDouble(commands[3]),
                        commands[4], commands[5], commands[6], LocalDate.parse(commands[7], DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                case "EditGame" -> gameService.editGame(Long.parseLong(commands[1]),
                        Arrays.stream(commands).skip(2).toArray(String[]::new));
                case "DeleteGame" -> gameService.deleteGame(Long.parseLong(commands[1]));
                case "AllGames" -> gameService.getAllGamesInfo();
                case "DetailGame" -> gameService.getGameInfo(commands[1]);
                case "OwnedGames" -> userService.getCurrentUserGames();
            }
        }
    }
}

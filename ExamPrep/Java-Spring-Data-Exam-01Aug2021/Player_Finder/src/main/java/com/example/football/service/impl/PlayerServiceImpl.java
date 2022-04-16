package com.example.football.service.impl;

import com.example.football.models.dto.PlayerSeedDto;
import com.example.football.models.dto.PlayerSeedRootDto;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String PLAYERS_FILE_PATH = "src/main/resources/files/xml/players.xml";

    private final PlayerRepository playerRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final StatService statService;
    private final TeamService teamService;
    private final TownService townService;

    public PlayerServiceImpl(PlayerRepository playerRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper, StatService statService, TeamService teamService, TownService townService) {
        this.playerRepository = playerRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.statService = statService;
        this.teamService = teamService;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder playersOutputMessage = new StringBuilder();
        PlayerSeedRootDto playerSeedRootDto = xmlParser.fromFile(PLAYERS_FILE_PATH, PlayerSeedRootDto.class);

        playerSeedRootDto.getPlayers()
                .stream()
                .filter(playerSeedDto -> {
                    return ensureIsValid(playersOutputMessage, playerSeedDto);
                })
                .map(playerSeedDto -> {
                    Player player = modelMapper.map(playerSeedDto, Player.class);
                    player.setStat(statService.findStatById(playerSeedDto.getStat().getId()));
                    player.setTeam(teamService.findTeamByName(playerSeedDto.getTeam().getName()));
                    player.setTown(townService.findTownByName(playerSeedDto.getTown().getName()));
                    return player;
                })
                .forEach(playerRepository::save);

        return playersOutputMessage.toString();
    }

    private boolean ensureIsValid(StringBuilder playersOutputMessage, PlayerSeedDto playerSeedDto) {
        if (validationUtil.isValid(playerSeedDto) &&
                !playerRepository.existsPlayerByEmail(playerSeedDto.getEmail())) {
            playersOutputMessage.append(String.format("Successfully imported Player %s %s - %s",
                            playerSeedDto.getFirstName(), playerSeedDto.getLastName(), playerSeedDto.getPosition().name()))
                    .append(System.lineSeparator());
            return true;
        }
        playersOutputMessage.append("Invalid Player").append(System.lineSeparator());
        return false;
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder output = new StringBuilder();
        playerRepository.findBestPlayers(LocalDate.of(1995, 1, 1),
                        LocalDate.of(2003, 1, 1))
                .forEach(player -> {
                    output.append(String.format("Player - %s %s\n" +
                                    "\tPosition - %s\n" +
                                    "\tTeam - %s\n" +
                                    "\tStadium - %s\n",
                            player.getFirstName(), player.getLastName(),
                            player.getPosition().name(),
                            player.getTeam().getName(),
                            player.getTeam().getStadiumName()));
                });

        return output.toString();
    }
}

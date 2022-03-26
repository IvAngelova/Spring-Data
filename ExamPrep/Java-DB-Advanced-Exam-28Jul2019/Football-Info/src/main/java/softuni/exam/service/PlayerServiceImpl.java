package softuni.exam.service;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.PlayerSeedDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class PlayerServiceImpl implements PlayerService {
    private static final String PLAYERS_FILE_PATH = "src/main/resources/files/json/players.json";

    private final ValidatorUtil validatorUtil;
    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final FileUtil fileUtil;
    private final PictureService pictureService;
    private final TeamService teamService;

    public PlayerServiceImpl(ValidatorUtil validatorUtil, PlayerRepository playerRepository, ModelMapper modelMapper, Gson gson, FileUtil fileUtil, PictureService pictureService, TeamService teamService) {
        this.validatorUtil = validatorUtil;
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.pictureService = pictureService;
        this.teamService = teamService;
    }

    @Override
    public String importPlayers() throws IOException {
        StringBuilder sb = new StringBuilder();

        PlayerSeedDto[] playerSeedDtos = gson.fromJson(readPlayersJsonFile(), PlayerSeedDto[].class);
        for (PlayerSeedDto playerSeedDto : playerSeedDtos) {

            if (validatorUtil.isValid(playerSeedDto)) {

                if (this.playerRepository.findByFirstNameAndLastNameAndNumber(playerSeedDto.getFirstName(),
                        playerSeedDto.getLastName(), playerSeedDto.getNumber()) == null) {

                    Player player = modelMapper.map(playerSeedDto, Player.class);

                    Picture pictureByUrl = pictureService.findPictureByUrl(playerSeedDto.getPicture().getUrl());
                    Team teamByName = teamService.findTeamByName(playerSeedDto.getTeam().getName());

                    if (pictureByUrl == null || teamByName == null) {
                        sb.append("Invalid player").append(System.lineSeparator());
                        continue;
                    }

                    player.setPicture(pictureByUrl);
                    player.setTeam(teamByName);

                    playerRepository.save(player);
                    sb.append(String.format("Successfully imported player - %s",
                            playerSeedDto.getFirstName() + " " + playerSeedDto.getLastName()));

                } else {
                    sb.append("Already in DB");
                }

            } else {
                sb.append("Invalid player");
            }

            sb.append(System.lineSeparator());
        }


        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return fileUtil.readFile(PLAYERS_FILE_PATH);
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder playersWithSalaryInfo = new StringBuilder();
        playerRepository.findAllBySalaryGreaterThanOrderBySalaryDesc(new BigDecimal(100000))
                .forEach(player -> {
                    playersWithSalaryInfo.append(String.format("Player name: %s %s \n" +
                                    "\tNumber: %d\n" +
                                    "\tSalary: %.2f\n" +
                                    "\tTeam: %s\n",
                            player.getFirstName(), player.getLastName(), player.getNumber(),
                            player.getSalary(), player.getTeam().getName()));
                });
        return playersWithSalaryInfo.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder playersInNorthHub = new StringBuilder();
        playersInNorthHub.append("Team: North Hub").append(System.lineSeparator());

        playerRepository
                .findAllByTeam_NameOrderByIdAsc("North Hub")
                .forEach(player -> {
                    playersInNorthHub.append(String.format("\tPlayer name: %s %s - %s\n" +
                                    "\tNumber: %d\n", player.getFirstName(), player.getLastName(), player.getPosition().name(),
                            player.getNumber()));
                });

        return playersInNorthHub.toString();
    }
}

package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.TeamSeedDto;
import softuni.exam.domain.dtos.TeamSeedRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class TeamServiceImpl implements TeamService {
    private static final String TEAMS_FILE_PATH = "src/main/resources/files/xml/teams.xml";

    private final TeamRepository teamRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final FileUtil fileUtil;
    private final PictureService pictureService;

    public TeamServiceImpl(TeamRepository teamRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidatorUtil validatorUtil, FileUtil fileUtil, PictureService pictureService) {
        this.teamRepository = teamRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.fileUtil = fileUtil;
        this.pictureService = pictureService;
    }

    @Override
    public String importTeams() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        TeamSeedRootDto teamSeedRootDto = xmlParser.fromFile(TEAMS_FILE_PATH, TeamSeedRootDto.class);

        for (TeamSeedDto teamSeedDto : teamSeedRootDto.getTeams()) {

            String url = teamSeedDto.getPicture().getUrl();
            Picture picture = pictureService.findPictureByUrl(url);
            if (picture == null) {
                sb.append("Invalid team").append(System.lineSeparator());
                continue;
            }

            boolean isValid = validatorUtil.isValid(teamSeedDto);
            if (isValid) {
                sb.append(String.format("Successfully imported team - %s", teamSeedDto.getName()))
                        .append(System.lineSeparator());

            } else {
                sb.append("Invalid team").append(System.lineSeparator());
                continue;
            }

            Team team = modelMapper.map(teamSeedDto, Team.class);
            team.setPicture(picture);
            teamRepository.save(team);
        }
        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return fileUtil
                .readFile(TEAMS_FILE_PATH);
    }

    @Override
    public Team findTeamByName(String name) {
        return teamRepository.findByName(name);
    }
}

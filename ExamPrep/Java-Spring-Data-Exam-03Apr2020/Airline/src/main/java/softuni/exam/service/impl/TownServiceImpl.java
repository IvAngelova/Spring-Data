package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.TownSeedDto;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TownServiceImpl implements TownService {
    private static final String TOWNS_FILE_PATH = "src/main/resources/files/json/towns.json";

    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public TownServiceImpl(TownRepository townRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder townsOutputInfo = new StringBuilder();

        TownSeedDto[] townSeedDtos = gson.fromJson(readTownsFileContent(), TownSeedDto[].class);

        Arrays.stream(townSeedDtos)
                .filter(townSeedDto -> {
                    return ensureIsValid(townsOutputInfo, townSeedDto);
                })
                .map(townSeedDto -> modelMapper.map(townSeedDto, Town.class))
                .forEach(townRepository::save);

        return townsOutputInfo.toString();
    }

    @Override
    public Town findByName(String name) {
        return townRepository.findTownByName(name);
    }

    private boolean ensureIsValid(StringBuilder townsOutputInfo, TownSeedDto townSeedDto) {
        if (validationUtil.isValid(townSeedDto) &&
                townRepository.findTownByName(townSeedDto.getName()) == null) {
            townsOutputInfo.append(String.format("Successfully imported Town %s - %d",
                            townSeedDto.getName(), townSeedDto.getPopulation()))
                    .append(System.lineSeparator());
            return true;
        }

        townsOutputInfo.append("Invalid Town").append(System.lineSeparator());
        return false;
    }
}

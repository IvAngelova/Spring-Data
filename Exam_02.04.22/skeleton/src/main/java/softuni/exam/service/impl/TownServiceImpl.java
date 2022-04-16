package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownSeedDto;
import softuni.exam.models.entity.Town;
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
        StringBuilder townsOutputMessage = new StringBuilder();

        TownSeedDto[] townSeedDtos = gson.fromJson(readTownsFileContent(), TownSeedDto[].class);

        Arrays.stream(townSeedDtos)
                .filter(townSeedDto -> {
                    return ensureIsValid(townsOutputMessage, townSeedDto);
                })
                .map(townSeedDto -> modelMapper.map(townSeedDto, Town.class))
                .forEach(townRepository::save);

        return townsOutputMessage.toString();
    }

    @Override
    public Town findTownByName(String name) {
        return townRepository.findByTownName(name);
    }

    private boolean ensureIsValid(StringBuilder townsOutputMessage, TownSeedDto townSeedDto) {
        boolean isValid = validationUtil.isValid(townSeedDto) &&
                !townRepository.existsByTownName(townSeedDto.getTownName());
        if (isValid) {
            townsOutputMessage.append(String.format("Successfully imported town %s - %d",
                    townSeedDto.getTownName(), townSeedDto.getPopulation()));
        } else {
            townsOutputMessage.append("Invalid town");
        }

        townsOutputMessage.append(System.lineSeparator());

        return isValid;
    }
}

package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.TownSeedDto;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.service.TownService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

import static hiberspring.common.Constants.PATH_TO_FILES;

@Service
public class TownServiceImpl implements TownService {
    private static final String TOWNS_FILE_NAME = "towns.json";

    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final FileUtil fileUtil;

    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, FileUtil fileUtil) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return fileUtil.readFile(PATH_TO_FILES + TOWNS_FILE_NAME);
    }

    @Override
    public String importTowns(String townsFileContent) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        TownSeedDto[] townSeedDtos = gson.fromJson(readTownsJsonFile(), TownSeedDto[].class);

        Arrays.stream(townSeedDtos)
                .filter(townSeedDto -> {
                    boolean isValid = validationUtil.isValid(townSeedDto);
                    stringBuilder.append(isValid
                            ? String.format("Successfully imported Town %s.", townSeedDto.getName())
                            : "Error: Invalid data.")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(townSeedDto -> modelMapper.map(townSeedDto, Town.class))
                .forEach(townRepository::save);

        return stringBuilder.toString();
    }

    @Override
    public Town findTownByName(String name) {
        return townRepository
                .findByName(name)
                .orElse(null);
    }
}

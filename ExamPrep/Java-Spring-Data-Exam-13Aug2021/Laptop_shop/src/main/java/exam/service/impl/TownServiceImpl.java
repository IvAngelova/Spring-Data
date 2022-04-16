package exam.service.impl;

import exam.model.dto.TownSeedDto;
import exam.model.dto.TownSeedRootDto;
import exam.model.entity.Town;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TownServiceImpl implements TownService {
    private static final String TOWNS_FILE_PATH = "src/main/resources/files/xml/towns.xml";

    private final TownRepository townRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TownServiceImpl(TownRepository townRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
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
    public String importTowns() throws JAXBException, FileNotFoundException {
        StringBuilder townsOutputInfo = new StringBuilder();

        TownSeedRootDto townSeedRootDto = xmlParser.fromFile(TOWNS_FILE_PATH, TownSeedRootDto.class);

        townSeedRootDto.getTowns()
                .stream()
                .filter(townSeedDto -> {
                    return ensureIsValid(townsOutputInfo, townSeedDto);
                })
                .map(townSeedDto -> modelMapper.map(townSeedDto, Town.class))
                .forEach(townRepository::save);

        return townsOutputInfo.toString();
    }

    @Override
    public Town findTownByName(String name) {
        return townRepository.findTownByName(name);
    }

    private boolean ensureIsValid(StringBuilder townsOutputInfo, TownSeedDto townSeedDto) {
        if (validationUtil.isValid(townSeedDto) && !townRepository.existsTownByName(townSeedDto.getName())) {
            townsOutputInfo.append(String.format("Successfully imported Town %s", townSeedDto.getName()))
                    .append(System.lineSeparator());
            return true;
        }

        townsOutputInfo.append("Invalid Town").append(System.lineSeparator());
        return false;
    }
}

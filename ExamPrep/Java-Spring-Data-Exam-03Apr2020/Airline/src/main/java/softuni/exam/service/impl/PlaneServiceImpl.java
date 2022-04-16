package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.PlaneSeedDto;
import softuni.exam.models.dtos.PlaneSeedRootDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlaneServiceImpl implements PlaneService {

    private static final String PLANES_FILE_PATH = "src/main/resources/files/xml/planes.xml";

    private final PlaneRepository planeRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public PlaneServiceImpl(PlaneRepository planeRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.planeRepository = planeRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANES_FILE_PATH));
    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {
        StringBuilder planesOutputInfo = new StringBuilder();

        PlaneSeedRootDto planeSeedRootDto = xmlParser.fromFile(PLANES_FILE_PATH, PlaneSeedRootDto.class);

        planeSeedRootDto.getPlanes()
                .stream()
                .filter(planeSeedDto -> {
                    return ensureIsValid(planesOutputInfo, planeSeedDto);
                })
                .map(planeSeedDto -> modelMapper.map(planeSeedDto, Plane.class))
                .forEach(planeRepository::save);

        return planesOutputInfo.toString();
    }

    @Override
    public Plane findPlaneByRegisterNumber(String registerNumber) {
        return planeRepository.findByRegisterNumber(registerNumber);
    }

    private boolean ensureIsValid(StringBuilder planesOutputInfo, PlaneSeedDto planeSeedDto) {
        if (validationUtil.isValid(planeSeedDto) &&
                planeRepository.findByRegisterNumber(planeSeedDto.getRegisterNumber()) == null) {
            planesOutputInfo.append(String.format("Successfully imported Plane %s",
                            planeSeedDto.getRegisterNumber()))
                    .append(System.lineSeparator());
            return true;
        }

        planesOutputInfo.append("Invalid Plane").append(System.lineSeparator());
        return false;
    }
}

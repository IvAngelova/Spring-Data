package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentSeedDto;
import softuni.exam.models.dto.ApartmentSeedRootDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private static final String APARTMENTS_FILE_PATH = "src/main/resources/files/xml/apartments.xml";

    private final ApartmentRepository apartmentRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final TownService townService;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper, TownService townService) {
        this.apartmentRepository = apartmentRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(APARTMENTS_FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder apartmentsOutputMessage = new StringBuilder();

        ApartmentSeedRootDto apartmentSeedRootDto = xmlParser.fromFile(APARTMENTS_FILE_PATH, ApartmentSeedRootDto.class);
        apartmentSeedRootDto.getApartments()
                .stream()
                .filter(apartmentSeedDto -> {
                    return ensureIsValid(apartmentsOutputMessage, apartmentSeedDto);
                })
                .map(apartmentSeedDto -> {
                    Apartment apartment = modelMapper.map(apartmentSeedDto, Apartment.class);
                    apartment.setTown(townService.findTownByName(apartmentSeedDto.getTown()));
                    return apartment;
                })
                .forEach(apartmentRepository::save);

        return apartmentsOutputMessage.toString();
    }

    @Override
    public Apartment findApartmentById(Integer id) {
        return apartmentRepository.findById(id).orElse(null);
    }

    private boolean ensureIsValid(StringBuilder apartmentsOutputMessage, ApartmentSeedDto apartmentSeedDto) {
        boolean isValid = validationUtil.isValid(apartmentSeedDto) &&
                !apartmentRepository.existsByAreaAndTown_TownName(apartmentSeedDto.getArea(),
                        apartmentSeedDto.getTown());
        if (isValid) {
            apartmentsOutputMessage.append(String.format("Successfully imported apartment %s - %.2f",
                    apartmentSeedDto.getApartmentType().name(), apartmentSeedDto.getArea()));
        } else {
            apartmentsOutputMessage.append("Invalid apartment");
        }

        apartmentsOutputMessage.append(System.lineSeparator());

        return isValid;
    }
}

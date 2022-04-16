package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.EmployeeCardSeedDto;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {
    private static final String EMPLOYEE_CARDS_FILE_NAME = "employee-cards.json";

    private final EmployeeCardRepository employeeCardRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final FileUtil fileUtil;

    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, FileUtil fileUtil) {
        this.employeeCardRepository = employeeCardRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return employeeCardRepository.count() > 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return fileUtil.readFile(PATH_TO_FILES + EMPLOYEE_CARDS_FILE_NAME);
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();
        EmployeeCardSeedDto[] employeeCardSeedDtos = gson.fromJson(readEmployeeCardsJsonFile(), EmployeeCardSeedDto[].class);

        for (EmployeeCardSeedDto employeeCardSeedDto : employeeCardSeedDtos) {
            if (validationUtil.isValid(employeeCardSeedDto)) {

                if (employeeCardRepository.findByNumber(employeeCardSeedDto.getNumber()).isEmpty()) {

                    EmployeeCard employeeCard = modelMapper.map(employeeCardSeedDto, EmployeeCard.class);
                    employeeCardRepository.save(employeeCard);

                    sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Employee Card", employeeCardSeedDto.getNumber()));
                } else {
                    sb.append(ALREADY_IN_DB);
                }
            } else {
                sb.append(INCORRECT_DATA_MESSAGE);
            }

            sb.append(System.lineSeparator());

        }

        return sb.toString();
    }

    @Override
    public EmployeeCard findCardByNumber(String number) {
        return employeeCardRepository
                .findByNumber(number)
                .orElse(null);
    }
}

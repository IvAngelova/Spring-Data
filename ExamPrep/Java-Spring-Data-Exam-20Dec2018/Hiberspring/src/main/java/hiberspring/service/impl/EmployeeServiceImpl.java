package hiberspring.service.impl;

import hiberspring.domain.dtos.EmployeeSeedDto;
import hiberspring.domain.dtos.EmployeeSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.BranchService;
import hiberspring.service.EmployeeCardService;
import hiberspring.service.EmployeeService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static hiberspring.common.Constants.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final String EMPLOYEES_FILE_NAME = "employees.xml";

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final FileUtil fileUtil;
    private final BranchService branchService;
    private final EmployeeCardService employeeCardService;


    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, FileUtil fileUtil, BranchService branchService, EmployeeCardService employeeCardService) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.fileUtil = fileUtil;
        this.branchService = branchService;
        this.employeeCardService = employeeCardService;
    }

    @Override
    public Boolean employeesAreImported() {
        return employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return fileUtil.readFile(PATH_TO_FILES + EMPLOYEES_FILE_NAME);
    }

    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        EmployeeSeedRootDto employeeSeedRootDto = xmlParser
                .parseXml(EmployeeSeedRootDto.class, PATH_TO_FILES + EMPLOYEES_FILE_NAME);

        List<EmployeeSeedDto> employeeSeedDtos = employeeSeedRootDto.getEmployees();

        for (EmployeeSeedDto employeeSeedDto : employeeSeedDtos) {
            if (validationUtil.isValid(employeeSeedDto)) {

                if (employeeRepository.findEmployeeByCard_Number(employeeSeedDto.getCard()) == null) {

                    Employee employee = modelMapper.map(employeeSeedDto, Employee.class);
                    Branch branchByName = branchService.findBranchByName(employeeSeedDto.getBranch());
                    EmployeeCard cardByNumber = employeeCardService.findCardByNumber(employeeSeedDto.getCard());

                    if (branchByName == null || cardByNumber == null) {
                        sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                        continue;
                    }

                    employee.setBranch(branchByName);
                    employee.setCard(cardByNumber);
                    employeeRepository.save(employee);

                    sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,
                            "Employee", employeeSeedDto.getFirstName() + " " + employeeSeedDto.getLastName()));

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
    public String exportProductiveEmployees() {
        StringBuilder sb = new StringBuilder();

        employeeRepository.findAllEmployeesWithBranchWithMinOneProduct()
                .forEach(productiveEmployeeDto -> {
                    sb.append(String.format("Name: %s\n" +
                                    "Position: %s\n" +
                                    "Card Number: %s\n" +
                                    "-------------------------\n",
                            productiveEmployeeDto.getFullName(),
                            productiveEmployeeDto.getPosition(),
                            productiveEmployeeDto.getCardNumber()));
                });

        return sb.toString();
    }
}

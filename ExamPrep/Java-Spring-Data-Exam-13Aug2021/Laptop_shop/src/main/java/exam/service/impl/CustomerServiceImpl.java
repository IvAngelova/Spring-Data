package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.CustomerSeedDto;
import exam.model.entity.Customer;
import exam.repository.CustomerRepository;
import exam.service.CustomerService;
import exam.service.TownService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final String CUSTOMERS_FILE_PATH = "src/main/resources/files/json/customers.json";

    private final CustomerRepository customerRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final TownService townService;

    public CustomerServiceImpl(CustomerRepository customerRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, TownService townService) {
        this.customerRepository = customerRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(Path.of(CUSTOMERS_FILE_PATH));
    }

    @Override
    public String importCustomers() throws IOException {
        StringBuilder customersOutputInfo = new StringBuilder();

        CustomerSeedDto[] customerSeedDtos = gson.fromJson(readCustomersFileContent(), CustomerSeedDto[].class);

        Arrays.stream(customerSeedDtos)
                .filter(customerSeedDto -> {
                    return ensureIsValid(customersOutputInfo, customerSeedDto);
                })
                .map(customerSeedDto -> {
                    Customer customer = modelMapper.map(customerSeedDto, Customer.class);
                    customer.setTown(townService.findTownByName(customerSeedDto.getTown().getName()));
                    return customer;
                })
                .forEach(customerRepository::save);

        return customersOutputInfo.toString();
    }

    private boolean ensureIsValid(StringBuilder customersOutputInfo, CustomerSeedDto customerSeedDto) {
        if (validationUtil.isValid(customerSeedDto) && !customerRepository.existsCustomerByEmail(customerSeedDto.getEmail())) {
            customersOutputInfo.append(String.format("Successfully imported customer %s %s - %s",
                            customerSeedDto.getFirstName(), customerSeedDto.getLastName(), customerSeedDto.getEmail()))
                    .append(System.lineSeparator());
            return true;
        }

        customersOutputInfo.append("Invalid customer").append(System.lineSeparator());
        return false;
    }
}

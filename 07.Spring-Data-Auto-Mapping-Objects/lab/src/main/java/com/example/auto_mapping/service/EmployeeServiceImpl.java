package com.example.auto_mapping.service;

import com.example.auto_mapping.model.dto.EmployeeDto;
import com.example.auto_mapping.model.entity.Employee;
import com.example.auto_mapping.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public void persist(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeDto> printAllEmployeesBornBefore(int year) {
        LocalDate localDate = LocalDate.of(year, 1, 1);

        List<Employee> employees = employeeRepository
                .findAllByBirthdayBeforeOrderBySalaryDesc(localDate);

        ModelMapper mapper = new ModelMapper();

        return employees
                .stream()
                .map(e -> mapper.map(e, EmployeeDto.class))
                .collect(Collectors.toList());
    }
}

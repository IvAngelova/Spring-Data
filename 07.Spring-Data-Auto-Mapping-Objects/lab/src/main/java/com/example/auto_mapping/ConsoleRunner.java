package com.example.auto_mapping;

import com.example.auto_mapping.model.dto.EmployeeDto;
import com.example.auto_mapping.model.entity.Employee;
import com.example.auto_mapping.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final EmployeeService employeeService;

    @Autowired
    public ConsoleRunner(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @Override
    public void run(String... args) throws Exception {

        // persistToDB();

        List<EmployeeDto> employees = employeeService.printAllEmployeesBornBefore(1990);
        employees.forEach(System.out::println);

    }

    private void persistToDB() {

        Employee manager = new Employee(
                "Mrs.",
                "Manager",
                BigDecimal.ONE,
                LocalDate.of(1971, 7, 23),
                null);

        Employee first = new Employee(
                "first",
                "last",
                BigDecimal.TEN,
                LocalDate.of(1989, 5, 15),
                manager);

        Employee second = new Employee(
                "second",
                "last",
                BigDecimal.TEN,
                LocalDate.of(1995, 3, 12),
                manager);

        employeeService.persist(manager);
        employeeService.persist(first);
        employeeService.persist(second);
    }
}

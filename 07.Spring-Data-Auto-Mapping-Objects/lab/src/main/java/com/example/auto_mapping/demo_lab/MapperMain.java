package com.example.auto_mapping.demo_lab;

import com.example.auto_mapping.demo_lab.dto.EmployeeDTO;
import com.example.auto_mapping.demo_lab.dto.ManagerDTO;
import com.example.auto_mapping.demo_lab.entity.Address;
import com.example.auto_mapping.demo_lab.entity.Employee;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MapperMain {
    public static void main(String[] args) {

        Address address = new Address("hristo botev", 23, "sofia", "bulgaria");
        Employee first = new Employee("first",
                "last", BigDecimal.TEN, LocalDate.now(), address, true);

        Employee second = new Employee("second",
                "last", BigDecimal.ONE, LocalDate.now(), address, false);

        Employee manager = new Employee("manager",
                "last", BigDecimal.TEN, LocalDate.now(), address, true);

        manager.addEmployee(first);
        manager.addEmployee(second);

        ModelMapper mapper = new ModelMapper();
        ManagerDTO managerDTO = mapper.map(manager, ManagerDTO.class);

        System.out.println(managerDTO);

    }
}

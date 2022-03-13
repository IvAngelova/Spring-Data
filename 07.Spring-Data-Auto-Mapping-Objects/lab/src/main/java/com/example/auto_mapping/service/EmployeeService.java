package com.example.auto_mapping.service;

import com.example.auto_mapping.model.dto.EmployeeDto;
import com.example.auto_mapping.model.entity.Employee;

import java.util.List;

public interface EmployeeService {
    void persist(Employee employee);

    List<EmployeeDto> printAllEmployeesBornBefore(int year);
}

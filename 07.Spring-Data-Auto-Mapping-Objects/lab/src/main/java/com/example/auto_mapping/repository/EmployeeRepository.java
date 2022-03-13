package com.example.auto_mapping.repository;

import com.example.auto_mapping.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByBirthdayBeforeOrderBySalaryDesc(LocalDate birthday);
}

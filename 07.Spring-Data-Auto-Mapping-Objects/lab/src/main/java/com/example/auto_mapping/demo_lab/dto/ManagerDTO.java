package com.example.auto_mapping.demo_lab.dto;

import java.util.Set;
import java.util.stream.Collectors;

public class ManagerDTO {
    private String firstName;
    private String lastName;
    private Set<EmployeeDTO> subordinates;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<EmployeeDTO> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Set<EmployeeDTO> subordinates) {
        this.subordinates = subordinates;
    }

    @Override
    public String toString() {
        return String.format("%s %s | Employees: %d%n" +
                        "%s", this.firstName, this.lastName, this.subordinates.size(),
                this.subordinates
                        .stream()
                        .map(empDTO -> String.format("\t- %s", empDTO.toString()))
                        .collect(Collectors.joining("\n")));
    }
}

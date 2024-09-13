package com.payrollsystem.model;

import com.payrollsystem.dto.EmployeeDTO;
import lombok.Data;

@Data
public class Employee {
    private String empId;
    private String firstName;
    private String lastName;
    private String designation;
    private double salary;

    public Employee(EmployeeDTO dto) {
        this.empId = dto.getEmpId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.designation = dto.getDesignation();
        this.salary = dto.getSalary();
    }
}

package com.payrollsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFinancialReportDTO {
    private String empId;
    private String firstName;
    private String lastName;
    private double totalAmountPaid;
}

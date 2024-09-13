package com.payrollsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyReportDTO {
    private String month;
    private double totalSalary;
    private int totalEmployees;
}

package com.payrollsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAmountReportDTO {
    public String month;
    public double totalAmount;
    public int totalEmployees;
}

package com.payrollsystem.model;

import com.payrollsystem.dto.MonthlyReportDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MonthlyReport {
    private String month;
    private double totalSalary;
    private int totalEmployees;

    public MonthlyReport(MonthlyReportDTO dto){
        this.month = dto.getMonth();
        this.totalSalary = dto.getTotalSalary();
        this.totalEmployees = dto.getTotalEmployees();
    }
}

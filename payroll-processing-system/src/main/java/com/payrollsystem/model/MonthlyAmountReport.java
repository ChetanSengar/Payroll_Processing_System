package com.payrollsystem.model;

import com.payrollsystem.dto.MonthlyAmountReportDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MonthlyAmountReport {
    private String month;
    double totalAmount;
    int totalEmployees;

    public MonthlyAmountReport(MonthlyAmountReportDTO dto){
        this.month = dto.getMonth();
        this.totalAmount = dto.getTotalAmount();
        this.totalEmployees = dto.getTotalEmployees();
    }
}

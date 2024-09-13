package com.payrollsystem.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearlyFinancialReportDTO {
    private String event;
    private String empId;
    private Date eventDate;
    private double eventValue;
}

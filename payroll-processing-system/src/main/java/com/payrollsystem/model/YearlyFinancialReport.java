package com.payrollsystem.model;

import com.payrollsystem.dto.YearlyFinancialReportDTO;
import lombok.Data;

import java.util.Date;

@Data
public class YearlyFinancialReport {
    private String event;
    private String empId;
    private Date eventDate;
    private double eventValue;

    public YearlyFinancialReport(YearlyFinancialReportDTO dto){
        this.event = dto.getEvent();
        this.empId = dto.getEmpId();
        this.eventDate = dto.getEventDate();
        this.eventValue = dto.getEventValue();
    }
}

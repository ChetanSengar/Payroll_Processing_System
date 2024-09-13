package com.payrollsystem.model;

import com.payrollsystem.dto.EmployeeFinancialReportDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class EmployeeFinancialReport {
    private String empId;
    private String firstName;
    private String lastName;
    private double totalAmountPaid;

    public EmployeeFinancialReport(EmployeeFinancialReportDTO dto){
        this.empId = dto.getEmpId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.totalAmountPaid = dto.getTotalAmountPaid();
    }
}

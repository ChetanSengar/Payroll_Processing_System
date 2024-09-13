package com.payrollsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private String sequenceNo;
    private String empId;
    private String firstName;
    private String lastName;
    private String designation;
    private String event;
    private double value;
    private LocalDate eventDate;
    private String notes;
}

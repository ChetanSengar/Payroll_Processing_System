package com.payrollsystem.model;

import com.payrollsystem.dto.EventDTO;
import lombok.*;

import java.time.LocalDate;

@Data
public class Event {
    private String sequenceNo;
    private String empId;
    private String firstName;
    private String lastName;
    private String designation;
    private String event;
    private double value;
    private LocalDate eventDate;
    private String notes;

    public Event(EventDTO dto){
        this.sequenceNo=dto.getSequenceNo();
        this.empId = dto.getEmpId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.designation = dto.getDesignation();
        this.event = dto.getEvent();
        this.value = dto.getValue();
        this.eventDate = dto.getEventDate();
        this.notes = dto.getNotes();
    }
}
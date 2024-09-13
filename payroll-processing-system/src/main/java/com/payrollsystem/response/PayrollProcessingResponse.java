package com.payrollsystem.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.payrollsystem.dto.EventDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Response class specific to payroll processing operations.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class PayrollProcessingResponse extends BaseResponse {

    //    private String errorDescription;
    private List<EventDTO> events;
    private List<String> errorDescriptions;


    public PayrollProcessingResponse(final BaseResponse br) {
        super(br);
    }

    public PayrollProcessingResponse(final boolean success) {
        super(success);
    }

    public PayrollProcessingResponse(final boolean success, final List<EventDTO> events) {
        super(success);
        this.events = events;
    }

    public PayrollProcessingResponse(final boolean success, final List<String> errorDescriptions, final boolean isErrors) {
        super(success);
        this.errorDescriptions = errorDescriptions;
    }
}

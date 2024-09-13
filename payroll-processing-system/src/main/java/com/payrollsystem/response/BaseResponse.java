package com.payrollsystem.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * Generic response base class for payroll system interactions.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse implements Serializable {

    private boolean success;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("reason_code")
    private String reasonCode;

    @JsonProperty("reason_desc")
    private String reasonDesc;

    public BaseResponse() {
        transactionId = UUID.randomUUID().toString();
    }

    public BaseResponse(final BaseResponse baseResponse) {
        success = baseResponse.isSuccess();
        transactionId = baseResponse.getTransactionId();
        reasonCode = baseResponse.getReasonCode();
        reasonDesc = baseResponse.getReasonDesc();
    }

    public BaseResponse(final BaseResponse br, final boolean success) {
        this(br);
        this.success = success;
    }

    public BaseResponse(final boolean success) {
        this();
        this.success = success;
    }

    public BaseResponse(final String message, final boolean success) {
        this();
        this.success = success;
        reasonDesc = message;
    }
}


package com.payrollsystem.exception;

import com.payrollsystem.response.BaseResponse;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum defining custom exceptions with associated error codes and descriptions.
 */
@Getter
public enum CustomExceptionEnum {

    NO_FILE_FOUND("400-002", "No file uploaded or file is empty"),
    ERROR_PROCESSING_FILE("400-003", "Error processing file"),
    INTERNAL_EXCEPTION("400-001", "Internal Error has been occurred.");

    private static final Map<String, CustomExceptionEnum> map = new HashMap<>();
    private final String exceptionCode;
    private final String exceptionDescription;

    static {
        for (final CustomExceptionEnum enu : values()) {
            map.put(enu.getExceptionCode(), enu);
        }
    }


    CustomExceptionEnum(final String exceptionCode, final String exceptionDescription) {
        this.exceptionCode = exceptionCode;
        this.exceptionDescription = exceptionDescription;
    }

    /**
     * Retrieves the corresponding enum from a given error code.
     *
     * @param code the error code.
     * @return the matching enum.
     */
    public static CustomExceptionEnum getEnumFromCode(final String code) {
        return map.get(code);
    }

    /**
     * Creates a response detailing the exception.
     *
     * @return a populated BaseResponse object.
     */
    public BaseResponse getResponse() {
        final BaseResponse response = new BaseResponse();
        response.setSuccess(false);
        response.setReasonCode(exceptionCode);
        response.setReasonDesc(exceptionDescription);
        return response;
    }

}

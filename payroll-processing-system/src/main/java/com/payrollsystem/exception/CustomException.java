package com.payrollsystem.exception;

import lombok.Getter;

/**
 * Custom exception class for specific error handling.
 */
@Getter
public class CustomException extends Exception {

    private final String errorCode;

    /**
     * Constructor for CustomException.
     *
     * @param message   the detail message.
     * @param errorCode the error code associated with the exception.
     */
    public CustomException(final String message, final String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
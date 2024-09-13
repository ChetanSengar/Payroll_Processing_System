package com.payrollsystem.exception;

import com.payrollsystem.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler to catch and process known exceptions.
 */
public class GlobalExceptionHandler {

    /**
     * Handles generic exceptions.
     *
     * @param ex the exception
     * @return a response entity encapsulating the error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleAllExceptions(final Exception ex) {
        final BaseResponse response = CustomExceptionEnum.INTERNAL_EXCEPTION.getResponse();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles custom defined exceptions.
     *
     * @param ex the custom exception
     * @return a response entity encapsulating the error details
     */
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<BaseResponse> handleCustomException(final CustomException ex) {
        final BaseResponse response = new BaseResponse();
        response.setSuccess(false);
        response.setReasonDesc(ex.getMessage());
        response.setReasonCode(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

package com.sergeev.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

public class InvalidDataException extends RestException {

    private static final String MESSAGE_ERROR = "Exception.invalid";
    private static final int ERROR_CODE = 40409;

    /**
     * Instantiates a new Resource found exception.
     *
     * @param objectError the object error
     */
    public InvalidDataException(ObjectError objectError) {
        super(MESSAGE_ERROR, objectError, ERROR_CODE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public String getMessage() {
        return super.getDescription().getDefaultMessage();
    }

    @Override
    public int getErrorCode() {
        return ERROR_CODE;
    }
}

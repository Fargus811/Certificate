package com.sergeev.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

/**
 * The type Resource found exception.
 */
public class ResourceFoundException extends RestException {

    private static final String MESSAGE_ERROR = "Exception.recourseFound";
    private static final int ERROR_CODE = 40409;

    /**
     * Instantiates a new Resource found exception.
     *
     * @param objectError the object error
     */
    public ResourceFoundException(ObjectError objectError) {
        super(MESSAGE_ERROR, objectError, ERROR_CODE, HttpStatus.CONFLICT);
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

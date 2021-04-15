package com.sergeev.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

/**
 * The type Resource not found exception.
 */
public class ResourceIdNotFoundException extends RestException {

    private static final String MESSAGE_ERROR = "Exception.recourseNotFound";
    private static final int ERROR_CODE = 41409;

    @Override
    public String getMessage() {
        return super.getDescription().getDefaultMessage();
    }

    @Override
    public int getErrorCode() {
        return ERROR_CODE;
    }

    /**
     * Instantiates a new Resource id not found exception.
     *
     * @param objectError the object error
     */
    public ResourceIdNotFoundException(ObjectError objectError) {
        super(MESSAGE_ERROR, objectError, ERROR_CODE, HttpStatus.NOT_FOUND);
    }
}

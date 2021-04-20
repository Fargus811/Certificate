package com.sergeev.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

/**
 * The type Authenticate exception.
 */
public class AuthenticateException extends RestException {

    private static final String MESSAGE_ERROR = "Exception.recourseNotFound";
    private static final int ERROR_CODE = 40401;

    /**
     * Instantiates a new Authenticate exception.
     *
     * @param reason      the reason
     * @param description the description
     */
    public AuthenticateException(String reason, ObjectError description) {
        super(reason, description, ERROR_CODE, HttpStatus.NOT_FOUND);
    }
}

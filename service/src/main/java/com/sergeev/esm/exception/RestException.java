package com.sergeev.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

/**
 * The type Rest exception.
 */
@Getter
@Setter
@AllArgsConstructor
public class RestException extends RuntimeException {

    private final String reason;
    private final ObjectError description;
    private final int errorCode;
    private final HttpStatus httpStatus;
}

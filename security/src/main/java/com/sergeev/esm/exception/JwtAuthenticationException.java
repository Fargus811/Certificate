package com.sergeev.esm.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * The type Jwt authentication exception.
 */
public class JwtAuthenticationException extends AuthenticationException {

    /**
     * Instantiates a new Jwt authentication exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Jwt authentication exception.
     *
     * @param message the message
     */
    public JwtAuthenticationException(String message) {
        super(message);
    }
}

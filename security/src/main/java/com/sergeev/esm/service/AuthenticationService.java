package com.sergeev.esm.service;

import com.sergeev.esm.dto.AuthenticationRequest;

/**
 * The interface Authentication service.
 */
public interface AuthenticationService {

    /**
     * Generate token string.
     *
     * @param authenticationRequest the authentication request
     * @return the string
     */
    String generateToken(AuthenticationRequest authenticationRequest);
}

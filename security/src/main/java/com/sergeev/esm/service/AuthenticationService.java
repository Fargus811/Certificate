package com.sergeev.esm.service;

import com.sergeev.esm.dto.AuthenticationRequest;

public interface AuthenticationService {

    String generateToken(AuthenticationRequest authenticationRequest);
}

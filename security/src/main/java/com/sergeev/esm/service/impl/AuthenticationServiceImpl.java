package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.AuthenticationRequest;
import com.sergeev.esm.exception.AuthenticateException;
import com.sergeev.esm.provider.JwtTokenProvider;
import com.sergeev.esm.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

/**
 * The type Authentication service.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String generateToken(AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest);
        String username = authenticationRequest.getUsername();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenProvider.generateToken(userDetails);
    }

    private void authenticate(AuthenticationRequest authenticationRequest) {
        try {
            String username = authenticationRequest.getUsername();
            String password = authenticationRequest.getPassword();
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new AuthenticateException("Exception.recourseNotFound",
                    new ObjectError(authenticationRequest.toString(),
                            "Exception.certificateWithIdNotFounded"));
        }
    }
}

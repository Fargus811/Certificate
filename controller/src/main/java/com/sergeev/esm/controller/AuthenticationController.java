package com.sergeev.esm.controller;

import com.sergeev.esm.dto.AuthenticationRequest;
import com.sergeev.esm.dto.RegistrationForm;
import com.sergeev.esm.dto.UserDTO;
import com.sergeev.esm.service.AuthenticationService;
import com.sergeev.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v3")
@RequiredArgsConstructor
public class AuthenticationController {

    private final static String TOKEN_KEY = "token";

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> generate(@RequestBody AuthenticationRequest authenticationRequest) {
        String token = authenticationService.generateToken(authenticationRequest);
        Map<Object, Object> response = new HashMap<>();
        response.put(TOKEN_KEY, token);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/registration")
    public UserDTO register(@RequestBody RegistrationForm registrationForm) {
        UserDTO userDTO = userService.register(registrationForm);
        return userDTO;
    }
}

package com.sergeev.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Authentication request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    private String username;
    private String password;
}

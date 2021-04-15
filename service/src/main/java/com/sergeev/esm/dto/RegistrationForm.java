package com.sergeev.esm.dto;

import lombok.Data;

/**
 * The type Registration form.
 */
@Data
public class RegistrationForm  {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}

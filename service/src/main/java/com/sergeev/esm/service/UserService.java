package com.sergeev.esm.service;

import com.sergeev.esm.dto.RegistrationForm;
import com.sergeev.esm.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The interface User service.
 */
public interface UserService extends UserDetailsService {

    /**
     * Find by id user.
     *
     * @param id the id
     * @return the user dto
     */
    UserDTO findById(Long id);

    /**
     * Find all userDTO list.
     *
     * @return the list
     */
    Page<UserDTO> findAll(Pageable pageable);

    UserDTO register(RegistrationForm registrationForm);
}

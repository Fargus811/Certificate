package com.sergeev.esm.service;

import com.sergeev.esm.dto.RegistrationForm;
import com.sergeev.esm.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * The interface User service.
 */
public interface UserService {

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
     * @param pageable the pageable
     * @return the list
     */
    Page<UserDTO> findAll(Pageable pageable);


    /**
     * Register user in system.
     *
     * @param registrationForm the registration form
     * @return the user dto
     */
    UserDTO register(RegistrationForm registrationForm);
}

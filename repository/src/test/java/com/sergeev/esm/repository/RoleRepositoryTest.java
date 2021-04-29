package com.sergeev.esm.repository;

import com.sergeev.esm.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName() {
        //given & when
        String expectedRoleName = "ROLE_USER";

        //then
        Optional<Role> roleActual = roleRepository.findByName("ROLE_USER");

        assertEquals(expectedRoleName, roleActual.get().getName());
    }
}
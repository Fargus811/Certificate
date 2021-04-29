package com.sergeev.esm.repository;

import com.sergeev.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername() {
        //given & when
        String expectedUsername = "fargus";

        //then
        Optional<User> userActual = userRepository.findByUsername("fargus");

        assertEquals(expectedUsername, userActual.get().getUsername());
    }
}
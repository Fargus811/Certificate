package com.sergeev.esm.repository;

import com.sergeev.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findByUsername(String username);
}

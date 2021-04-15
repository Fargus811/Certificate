package com.sergeev.esm.repository;

import com.sergeev.esm.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Role repository.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find by name optional.
     *
     * @param roleName the role name
     * @return the optional
     */
    Optional<Role> findByName(String roleName);
}

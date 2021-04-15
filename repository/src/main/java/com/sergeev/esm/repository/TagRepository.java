package com.sergeev.esm.repository;

import com.sergeev.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * The interface Tag repository.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Find by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Tag> findByName(String name);

    /**
     * Find users most widely used tag optional.
     *
     * @return the optional
     */
    @Query(value =RepositoryConstants.FIND_USER_MOST_WIDELY_USED_TAG, nativeQuery = true)
    Optional<Tag> findUsersMostWidelyUsedTag();
}

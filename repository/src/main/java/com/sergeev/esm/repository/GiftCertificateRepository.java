package com.sergeev.esm.repository;

import com.sergeev.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * The interface Gift certificate repository.
 */
@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    /**
     * Find by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<GiftCertificate> findByName(String name);

    /**
     * Find all by tag names.
     *
     * @param tagNames the tag names
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = "SELECT c FROM GiftCertificate c LEFT JOIN c.tags t WHERE t.name IN :tagNames ")
    Page<GiftCertificate> findAllByTagNames(@Param(value = "tagNames") Set<String> tagNames, Pageable pageable);
}

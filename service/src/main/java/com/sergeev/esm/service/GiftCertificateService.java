package com.sergeev.esm.service;

import com.sergeev.esm.dto.GiftCertificateReturnDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * The interface Gift certificate service.
 */
public interface GiftCertificateService extends BaseService<GiftCertificateReturnDTO> {

    /**
     * Find all by tag names page.
     *
     * @param tagNames the tag names
     * @param pageable the pageable
     * @return the page
     */
    Page<GiftCertificateReturnDTO> findAllByTagNames(Set<String> tagNames, Pageable pageable);
}

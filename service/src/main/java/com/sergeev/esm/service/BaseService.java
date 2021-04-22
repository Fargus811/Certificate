package com.sergeev.esm.service;

import com.sergeev.esm.dto.AbstractDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Base service.
 *
 * @param <R> the type parameter
 */
public interface BaseService<R extends AbstractDTO> {

    /**
     * Find entity by id.
     *
     * @param id the id
     * @return the t
     */
    R findById(Long id);

    /**
     * Find all entity list.
     *
     * @param pageable the page of request
     * @return the list
     */
    Page<R> findAll(Pageable pageable);

    /**
     * Create or update entity.
     *
     * @param entity the entity
     * @return the t
     */
    <T extends AbstractDTO> R createOrUpdate(T entity);

    /**
     * Delete entity by id.
     *
     * @param id the id
     */
    void delete(Long id);
}

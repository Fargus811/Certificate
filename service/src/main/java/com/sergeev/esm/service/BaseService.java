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
     * Find by id t.
     *
     * @param id the id
     * @return the t
     */
    R findById(Long id);

    /**
     * Find all list.
     *
     * @param pageable the page of request
     * @return the list
     */
    Page<R> findAll(Pageable pageable);

    /**
     * Create t.
     *
     * @param entity the entity
     * @return the t
     */
    <T extends AbstractDTO> R create(T entity);

    /**
     * Update.
     *
     * @param entity the entity
     */
    <T extends AbstractDTO> R update(T entity);

    /**
     * Delete by entity by id.
     *
     * @param id the id
     */
    void delete(Long id);
}

package com.sergeev.esm.repository;

import com.sergeev.esm.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Order repository.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find by user id page.
     *
     * @param userId   the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);
}

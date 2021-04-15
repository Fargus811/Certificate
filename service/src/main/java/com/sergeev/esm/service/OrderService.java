package com.sergeev.esm.service;

import com.sergeev.esm.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Order service.
 */
public interface OrderService extends BaseService<OrderDTO> {

    /**
     * Read orders by user id list.
     *
     * @param userId      the user id
     * @param pageable the request page
     * @return the list
     */
    Page<OrderDTO> findOrdersByUserID(Long userId, Pageable pageable);
}

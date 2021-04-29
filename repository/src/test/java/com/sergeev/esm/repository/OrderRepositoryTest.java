package com.sergeev.esm.repository;

import com.sergeev.esm.entity.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findByUserId() {
        //given
        Long userId = 1L;
        int expectedSizeOfUserOrders = 1;

        //when & then
        Page<Order> ordersByUserId = orderRepository.findByUserId(1L, Pageable.unpaged());

        assertEquals(expectedSizeOfUserOrders, ordersByUserId.getTotalElements());
    }
}
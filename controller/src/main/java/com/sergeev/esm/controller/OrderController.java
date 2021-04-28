package com.sergeev.esm.controller;

import com.sergeev.esm.dto.CreateOrderDTO;
import com.sergeev.esm.dto.OrderDTO;
import com.sergeev.esm.service.OrderService;
import com.sergeev.esm.util.HATEOASBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;
import java.util.Optional;

/**
 * The type Order controller.
 */
@RestController
@Validated
@RequestMapping(value = "/api/v3/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    /**
     * Options response entity shows all the ways to manipulate the resource.
     *
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                .build();
    }
    /**
     * Find orders paged model.
     *
     * @param userId   the user id
     * @param pageable the pageable
     * @return the paged model
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PagedModel<OrderDTO> findOrders(@RequestParam(required = false) @Min(1L) @Max(5000L) Long userId,
                                           Pageable pageable) {
        Page<OrderDTO> orderDTOPage = Objects.isNull(userId) ? orderService.findAll(pageable) :
                orderService.findOrdersByUserId(userId, pageable);
        HATEOASBuilder.addLinksToListOrder(orderDTOPage.getContent(), pageable);
        return pagedResourcesAssembler.toModel(orderDTOPage);
    }

    /**
     * Find by id abstract dto.
     *
     * @param id the id
     * @return the abstract dto
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO findById(@PathVariable("id") @Min(1L) @Max(10000L) Long id) {
        OrderDTO orderDTO = orderService.findById(id);
        return HATEOASBuilder.addLinksToOrder(orderDTO);
    }

    /**
     * Create response entity.
     *
     * @param createOrderDTO the create order dto
     * @return the response entity
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN, ROLE_USER')")
    public ResponseEntity<OrderDTO> create(@RequestBody CreateOrderDTO createOrderDTO) {
        OrderDTO orderDTO = orderService.upsert(createOrderDTO);
        HATEOASBuilder.addLinksToOrder(orderDTO);
        return ResponseEntity.of(Optional.of(orderDTO));
    }
}

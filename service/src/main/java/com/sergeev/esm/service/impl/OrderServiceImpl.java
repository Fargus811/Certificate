package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.AbstractDTO;
import com.sergeev.esm.dto.CreateOrderDTO;
import com.sergeev.esm.dto.OrderDTO;
import com.sergeev.esm.entity.GiftCertificate;
import com.sergeev.esm.entity.Order;
import com.sergeev.esm.entity.User;
import com.sergeev.esm.exception.ResourceIdNotFoundException;
import com.sergeev.esm.repository.GiftCertificateRepository;
import com.sergeev.esm.repository.OrderRepository;
import com.sergeev.esm.repository.UserRepository;
import com.sergeev.esm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Order service.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    @Override
    public OrderDTO findById(Long id) {
        Optional<Order> optionalGiftCertificate = orderRepository.findById(id);
        if (optionalGiftCertificate.isEmpty()) {
            throw new ResourceIdNotFoundException(
                    new ObjectError(id.toString(), "Exception.orderWithIdNotFound"));
        }
        return modelMapper.map(optionalGiftCertificate.get(), OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> findOrdersByUserID(Long userId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);
        return getOrderDTOS(pageable, orderPage);
    }

    @Override
    public Page<OrderDTO> findAll(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return getOrderDTOS(pageable, orderPage);
    }

    private PageImpl<OrderDTO> getOrderDTOS(Pageable pageable, Page<Order> orderPage) {
        return new PageImpl<>(orderPage.get().map(order -> modelMapper.map(order, OrderDTO.class)).
                collect(Collectors.toList()), pageable, orderPage.getTotalElements());
    }

    @Override
    public OrderDTO createOrUpdate(AbstractDTO createOrderRequest) {
        CreateOrderDTO createOrderDTO = (CreateOrderDTO) createOrderRequest;
        long userId = createOrderDTO.getUserId();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResourceIdNotFoundException(
                    new ObjectError(userOptional.toString(), "Exception.userWithIdNotFound"));
        }
        User userOfOrder = userOptional.get();
        List<GiftCertificate> giftCertificateList = new ArrayList<>();
        BigDecimal price = countTotalPrice(createOrderDTO, giftCertificateList);
        Order order = Order.builder()
                .user(userOfOrder)
                .giftCertificateList(giftCertificateList)
                .orderDate(LocalDateTime.now())
                .price(price)
                .build();
        return modelMapper.map(orderRepository.save(order), OrderDTO.class);

    }

    private BigDecimal countTotalPrice(CreateOrderDTO createOrderDTO, List<GiftCertificate> giftCertificateList) {
        BigDecimal price = BigDecimal.ZERO;
        for (Long giftId : createOrderDTO.getGiftCertificateIdList()) {
            Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(giftId);
            if (giftCertificateOptional.isEmpty()) {
                throw new ResourceIdNotFoundException(
                        new ObjectError(giftId.toString(), "Exception.certificateWithIdNotFound"));
            }
            GiftCertificate giftCertificate = giftCertificateOptional.get();
            price = price.add(giftCertificate.getPrice());
            giftCertificateList.add(giftCertificate);
        }
        return price;
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException();
    }
}

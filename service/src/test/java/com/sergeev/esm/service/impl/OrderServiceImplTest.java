package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.CreateOrderDTO;
import com.sergeev.esm.dto.OrderDTO;
import com.sergeev.esm.dto.UserDTO;
import com.sergeev.esm.entity.GiftCertificate;
import com.sergeev.esm.entity.Order;
import com.sergeev.esm.entity.Tag;
import com.sergeev.esm.entity.User;
import com.sergeev.esm.exception.ResourceIdNotFoundException;
import com.sergeev.esm.repository.GiftCertificateRepository;
import com.sergeev.esm.repository.OrderRepository;
import com.sergeev.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        //given
        Order order = Order.builder().id(1L).build();
        OrderDTO orderDTO = OrderDTO.builder().id(1L).build();

        //when
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        //then
        assertEquals(orderDTO, orderService.findById(1L));
        verify(orderRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(order, OrderDTO.class);
        verifyNoMoreInteractions(orderRepository, modelMapper);
    }

    @Test
    void findByWrongIdThrowsResourceIdNotFoundExceptionTest() {
        //given & when
        when(orderRepository.findById(10000000L)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceIdNotFoundException.class, () ->
                orderService.findById(10000000L));
        verify(orderRepository, times(1)).findById(10000000L);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void findOrdersByUserID() {
        //given
        User user = User.builder().id(1L).build();
        Order order = Order.builder().id(1L).user(user).build();

        UserDTO userDTO = UserDTO.builder().id(1L).build();
        OrderDTO orderDTO = OrderDTO.builder().id(1L).user(userDTO).build();

        PageImpl<OrderDTO> orderDTOPage =
                new PageImpl<>(List.of(orderDTO));
        //when
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findByUserId(1L, Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(order)));

        //then
        assertEquals(orderDTOPage,
                orderService.findOrdersByUserId(1L, Pageable.unpaged()));
        verify(orderRepository, times(1)).findByUserId(1L, Pageable.unpaged());
        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(order, OrderDTO.class);
        verifyNoMoreInteractions(orderRepository, userRepository, modelMapper);
    }

    @Test
    void findOrdersByNotExistsUserThrowsResourceIdNotFoundException(){
        //given & when
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceIdNotFoundException.class, () ->
                orderService.findOrdersByUserId(1L, Pageable.unpaged()));
        verify(userRepository,times(1)).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }
    @Test
    void findAll() {
        //given
        Order orderFromDB = Order.builder().id(1L).build();

        OrderDTO orderDTO = OrderDTO.builder().id(1L).build();

        PageImpl<OrderDTO> orderDTOPage = new PageImpl<>(List.of(orderDTO));

        //when
        when(orderRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(orderFromDB)));
        when(modelMapper.map(orderFromDB, OrderDTO.class)).thenReturn(orderDTO);

        //then
        assertEquals(orderDTOPage, orderService.findAll(Pageable.unpaged()));
        verify(orderRepository, times(1)).findAll(Pageable.unpaged());
        verify(modelMapper, times(1)).map(orderFromDB, OrderDTO.class);
        verifyNoMoreInteractions(orderRepository, modelMapper);
    }

    @Test
    void createOrderSuccess() {
        //given
        User user = User.builder().id(1L).build();

        GiftCertificate giftCertificateFirstInOrder = GiftCertificate.builder()
                .id(1L)
                .name("testing")
                .description("description")
                .price(BigDecimal.valueOf(200.00))
                .duration(91)
                .build();

        GiftCertificate giftCertificateSecondInOrder = GiftCertificate.builder()
                .id(2L)
                .name("testing")
                .description("description")
                .price(BigDecimal.valueOf(200.00))
                .duration(91)
                .build();

        List<Long> giftCertificatesId = List.of(1L,2L);
        CreateOrderDTO createOrderDTO = CreateOrderDTO.builder()
                .userId(1L)
                .giftCertificateIdList(giftCertificatesId)
                .build();

        Order orderExpected = Order.builder()
                .id(1L)
                .giftCertificateList(List.of(giftCertificateFirstInOrder,giftCertificateSecondInOrder))
                .price(BigDecimal.valueOf(400.00))
                .build();

        OrderDTO orderDTO = OrderDTO.builder()
                .id(1L)
                .price(BigDecimal.valueOf(400.00))
                .build();

        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificateFirstInOrder));
        when(giftCertificateRepository.findById(2L)).thenReturn(Optional.of(giftCertificateSecondInOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(orderExpected);
        when(modelMapper.map(orderExpected, OrderDTO.class)).thenReturn(orderDTO);

        //then
        assertEquals(orderDTO, orderService.upsert(createOrderDTO));
        verify(userRepository, times(1)).findById(1L);
        verify(giftCertificateRepository, times(2)).findById(anyLong());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(modelMapper, times(1)).map(orderExpected, OrderDTO.class);
        verifyNoMoreInteractions(userRepository, giftCertificateRepository,orderRepository, modelMapper);
    }

    @Test
    void createOrderUserNotFoundThrowsResourceIdNotFoundException(){
        //given
        CreateOrderDTO createOrderDTO = CreateOrderDTO.builder()
                .userId(1L)
                .build();

        //when
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceIdNotFoundException.class, () ->
                orderService.upsert(createOrderDTO));
        verify(userRepository,times(1)).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createOrderCertificateNotFoundThrowsResourceIdNotFoundException(){
        //given
        User user = User.builder().id(1L).build();

        GiftCertificate giftCertificateFirstInOrder = GiftCertificate.builder()
                .id(1L)
                .name("testing")
                .description("description")
                .price(BigDecimal.valueOf(200.00))
                .duration(91)
                .build();

        List<Long> giftCertificatesId = List.of(1L,2L);
        CreateOrderDTO createOrderDTO = CreateOrderDTO.builder()
                .userId(1L)
                .giftCertificateIdList(giftCertificatesId)
                .build();

        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificateFirstInOrder));
        when(giftCertificateRepository.findById(2L)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceIdNotFoundException.class, () ->
                orderService.upsert(createOrderDTO));
        verify(userRepository, times(1)).findById(1L);
        verify(giftCertificateRepository, times(2)).findById(anyLong());
        verifyNoMoreInteractions(userRepository,giftCertificateRepository);
    }

    @Test
    void deleteThrowsUnsupportedOperationException(){
        assertThrows(UnsupportedOperationException.class, () ->
                orderService.delete(1L));
    }
}
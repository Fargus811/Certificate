package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.RegistrationForm;
import com.sergeev.esm.dto.UserDTO;
import com.sergeev.esm.entity.Role;
import com.sergeev.esm.entity.User;
import com.sergeev.esm.exception.ResourceFoundException;
import com.sergeev.esm.exception.ResourceIdNotFoundException;
import com.sergeev.esm.repository.RoleRepository;
import com.sergeev.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        //given
        User user = User.builder().id(1L).build();
        UserDTO userDTO = UserDTO.builder().id(1L).build();

        //then
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        //then
        assertEquals(userDTO, userService.findById(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(user, UserDTO.class);
        verifyNoMoreInteractions(userRepository, modelMapper);
    }

    @Test
    void findByWrongIdThrowsResourceIdNotFoundExceptionTest() {
        //given & when
        Mockito.when(userRepository.findById(10000000L)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceIdNotFoundException.class, () ->
                userService.findById(10000000L));
        verify(userRepository, times(1)).findById(10000000L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAll() {
        //given
        User userFromDB = User.builder().id(1L).build();

        UserDTO userDTO = UserDTO.builder().id(1L).build();

        PageImpl<UserDTO> userDTOPage = new PageImpl<>(List.of(userDTO));

        //when
        when(userRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(userFromDB)));
        when(modelMapper.map(userFromDB, UserDTO.class)).thenReturn(userDTO);

        //then
        assertEquals(userDTOPage, userService.findAll(Pageable.unpaged()));
        verify(userRepository, times(1)).findAll(Pageable.unpaged());
        verify(modelMapper, times(1)).map(userFromDB, UserDTO.class);
        verifyNoMoreInteractions(userRepository, modelMapper);
    }

    @Test
    void registerSuccess() {
        //given
        RegistrationForm registrationForm = RegistrationForm.builder()
                .email("example@gmail.com")
                .username("test")
                .firstName("Ivan")
                .lastName("Ivanov")
                .password("test")
                .username("test")
                .build();
        Role roleUser = Role.builder().id(1L).name("ROLE_USER").build();
        User user = User.builder()
                .email("example@gmail.com")
                .username("test")
                .firstName("Ivan")
                .lastName("Ivanov")
                .password("test")
                .roles(Set.of(roleUser))
                .build();

        UserDTO userDTO = UserDTO.builder()
                .email("example@gmail.com")
                .username("test")
                .firstName("Ivan")
                .lastName("Ivanov")
                .build();
        //when
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
        when(bCryptPasswordEncoder.encode("test")).thenReturn("test");
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        //then
        assertEquals(userDTO, userService.register(registrationForm));
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(roleRepository, times(1)).findByName(any(String.class));
        verify(bCryptPasswordEncoder, times(1)).encode(any(String.class));
        verify(userRepository, times(1)).save(user);
        verify(modelMapper, times(1)).map(user, UserDTO.class);
        verifyNoMoreInteractions(userRepository, roleRepository, bCryptPasswordEncoder, modelMapper);
    }

    @Test
    void registerWithSameUsernameThrowsResourceFoundException(){
        //given
        User user = User.builder()
                .email("example@gmail.com")
                .username("test")
                .firstName("Ivan")
                .lastName("Ivanov")
                .password("test")
                .build();

        RegistrationForm registrationForm = RegistrationForm.builder()
                .email("example@gmail.com")
                .username("test")
                .firstName("Ivan")
                .lastName("Ivanov")
                .password("test")
                .username("test")
                .build();

        //when
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

        //then
        assertThrows(ResourceFoundException.class, () ->
                userService.register(registrationForm));
    }
}
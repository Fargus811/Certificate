package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.RegistrationForm;
import com.sergeev.esm.dto.UserDTO;
import com.sergeev.esm.entity.Role;
import com.sergeev.esm.entity.User;
import com.sergeev.esm.exception.ResourceFoundException;
import com.sergeev.esm.exception.ResourceIdNotFoundException;
import com.sergeev.esm.repository.RoleRepository;
import com.sergeev.esm.repository.UserRepository;
import com.sergeev.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type User service.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String ROLE_USER_FROM_DB = "ROLE_USER";

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final ModelMapper modelMapper;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return getUserDTOPage(pageable, userPage);
    }

    private PageImpl<UserDTO> getUserDTOPage(Pageable pageable, Page<User> userPage) {
        return new PageImpl<>(userPage.get().map(user -> modelMapper.map(user, UserDTO.class)).
                collect(Collectors.toList()), pageable, userPage.getTotalElements());
    }

    @Override
    public UserDTO findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResourceIdNotFoundException(
                    new ObjectError(id.toString(), "Exception.userWithIdNotFound"));
        }
        User user = userOptional.get();
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO register(RegistrationForm userForm) {
        Optional<User> userOptional = userRepository.findByUsername(userForm.getUsername());
        if (userOptional.isPresent()) {
            throw new ResourceFoundException(new ObjectError(userForm.getUsername(),
                    "Exception.userWithNameFound"));
        }

        Role roleUser = roleRepository.findByName(ROLE_USER_FROM_DB)
                .orElseThrow(() -> new ResourceFoundException(new ObjectError(userForm.getUsername(),
                        "Exception.roleWithNameNotFound")));

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);

        User user = User.builder()
                .email(userForm.getEmail())
                .username(userForm.getUsername())
                .firstName(userForm.getFirstName())
                .lastName(userForm.getLastName())
                .roles(userRoles)
                .password(bCryptPasswordEncoder.encode(userForm.getPassword()))
                .build();

        User registeredUser = userRepository.save(user);
        return modelMapper.map(registeredUser, UserDTO.class);
    }
}


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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ObjectError;

import java.util.Collection;
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
                    new ObjectError(id.toString(), "Exception.userWithIdNotFounded"));
        }
        User user = userOptional.get();
        return modelMapper.map(user, UserDTO.class);
    }

    private User findByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username '%s' not found", username)));
    }

    @Override
    public UserDTO register(RegistrationForm userForm) {
        Optional<User> userOptional = userRepository.findByUsername(userForm.getUsername());
        if (userOptional.isPresent()) {
            throw new ResourceFoundException(new ObjectError(userForm.getUsername(),
                    "Exception.userWithNameFounded"));
        }

        Role roleUser = roleRepository.findByName(ROLE_USER_FROM_DB)
                .orElseThrow(() -> new ResourceFoundException(new ObjectError(userForm.getUsername(),
                        "Exception.roleWithNameNotFounded")));

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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roleSet) {
        return roleSet.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}


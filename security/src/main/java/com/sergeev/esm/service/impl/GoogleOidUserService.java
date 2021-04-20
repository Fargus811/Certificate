package com.sergeev.esm.service.impl;

import com.sergeev.esm.dto.GoogleUserInfo;
import com.sergeev.esm.entity.Role;
import com.sergeev.esm.entity.User;
import com.sergeev.esm.provider.JwtTokenProvider;
import com.sergeev.esm.repository.RoleRepository;
import com.sergeev.esm.repository.UserRepository;
import com.sergeev.esm.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleOidUserService extends OidcUserService {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String GOOGLE_PROVIDER = "GOOGLE";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailService userDetailService;


    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        try {
            GoogleUserInfo googleUserDTO = new GoogleUserInfo(oidcUser.getAttributes());
            Optional<User> userOptional = userRepository.findByUsername(googleUserDTO.getUserName());
            if (userOptional.isEmpty()) {
                User user = new User();
                user.setEmail(googleUserDTO.getEmail());
                user.setUsername(googleUserDTO.getUserName());
                user.setFirstName(googleUserDTO.getGoogleUserFirstName());
                user.setLastName(googleUserDTO.getGoogleUserLastName());
                Role userRole = roleRepository.findByName(ROLE_USER).orElseThrow(RuntimeException::new);
                user.setRoles(Set.of(userRole));
                String password = UUID.randomUUID().toString();
                user.setPassword(passwordEncoder.encode(password));
                user.setProvider(GOOGLE_PROVIDER);
                userRepository.save(user);
            }
//            Authentication auth = jwtTokenProvider.getAuthentication(userRequest.getAccessToken().getTokenValue(), "google");
            UserDetails userDetails = userDetailService.loadUserByUsername(googleUserDTO.getUserName());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//            String token = jwtTokenProvider.generateToken(userDetails);
//            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return oidcUser;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
}

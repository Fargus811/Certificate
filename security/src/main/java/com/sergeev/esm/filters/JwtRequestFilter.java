package com.sergeev.esm.filters;

import com.sergeev.esm.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * The type Jwt request filter.
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends GenericFilterBean {

    public static final String NAME_ATTRIBUTE = "name";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        if (token != null && jwtTokenProvider.validateToken(token)
                && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } else if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication()) &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            OidcUser principal = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String name = principal.getAttribute(NAME_ATTRIBUTE);
            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
            String generateToken = jwtTokenProvider.generateToken(userDetails);
            HttpServletResponse httpServletResponse = (HttpServletResponse) res;

            httpServletResponse.addHeader(AUTHORIZATION, BEARER_PREFIX + generateToken);
        }
        filterChain.doFilter(req, res);
    }
}

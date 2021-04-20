package com.sergeev.esm.filters;

import com.sergeev.esm.entity.User;
import com.sergeev.esm.provider.JwtTokenProvider;
import com.sergeev.esm.service.impl.UserDetailService;
import lombok.RequiredArgsConstructor;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        String provider = "default";
        if (((HttpServletRequest) req).getRequestURI().contains("google")) {
            provider = "google";
        }
        if (token != null && jwtTokenProvider.validateToken(token, provider)
                && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            Authentication auth = jwtTokenProvider.getAuthentication(token, provider);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } else if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
            OidcUser principal = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String name = principal.getAttribute("name");
            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
            String generateToken = jwtTokenProvider.generateToken(userDetails);
            HttpServletResponse httpServletResponse = (HttpServletResponse) res;
            httpServletResponse.addHeader(AUTHORIZATION, "Bearer " + generateToken);
        }
        filterChain.doFilter(req, res);
    }
}

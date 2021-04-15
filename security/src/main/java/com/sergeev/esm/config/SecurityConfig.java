package com.sergeev.esm.config;

import com.sergeev.esm.filters.JwtAuthenticationEntryPoint;
import com.sergeev.esm.filters.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The type Security config.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String URL_FOR_LOGIN = "/api/v3/login";
    private static final String URL_FOR_CERTIFICATES = "/api/v3/certificates";
    private static final String URL_FOR_REGISTRATION = "/api/v3/registration";

    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();

    }

    /**
     * Rest authentication entry point authentication entry point.
     *
     * @return the authentication entry point
     */
    @Bean
    public AuthenticationEntryPoint RESTAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(URL_FOR_LOGIN).permitAll()
                .antMatchers(URL_FOR_CERTIFICATES).permitAll()
                .antMatchers(URL_FOR_REGISTRATION).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

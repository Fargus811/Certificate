package com.sergeev.esm.config;

import com.sergeev.esm.filters.JwtAuthenticationEntryPoint;
import com.sergeev.esm.filters.JwtRequestFilter;
import com.sergeev.esm.service.impl.GoogleOidUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private static List<String> clients = Arrays.asList("google");
    private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";


    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final GoogleOidUserService googleOidUserService;

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
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                .clientRegistrationRepository(clientRegistrationRepository())
                .userInfoEndpoint().oidcUserService(googleOidUserService);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        String client = "google";
        String clientId ="523233826916-uj3h9smlrmi909rfvgpad3on53o0466f.apps.googleusercontent.com";
        String clientSecret = "iTYZK9h3OdgJAxiPvCysc9Lw";
        ClientRegistration clientRegistration = CommonOAuth2Provider.GOOGLE.getBuilder(client)
                .clientId(clientId).clientSecret(clientSecret).scope("openid", "email", "profile").build();
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}

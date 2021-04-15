package com.sergeev.esm.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * The type Service config.
 */
@Configuration
@ComponentScan("com.sergeev.esm.service")
public class ServiceConfig {

    /**
     * Model mapper model mapper.
     *
     * @return the model mapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

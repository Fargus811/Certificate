package com.sergeev.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * The type Spring boot gift start application.
 */

@SpringBootApplication
public class SpringBootGiftApplication {

    /**
     * The entry point of application to start.
     *
     * @param args the input arguments of application
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootGiftApplication.class, args);
    }
}

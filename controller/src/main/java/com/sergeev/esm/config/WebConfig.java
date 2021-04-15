package com.sergeev.esm.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.apache.commons.lang3.CharEncoding.UTF_8;

/**
 * The type Web config.
 */
@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebConfig {

    private static final String NAME_FOR_MESSAGE_SOURCE = "classpath:messages";

    /**
     * Message source for custom exceptions message.
     *
     * @return the message source
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(NAME_FOR_MESSAGE_SOURCE);
        messageSource.setDefaultEncoding(UTF_8);
        return messageSource;
    }
}

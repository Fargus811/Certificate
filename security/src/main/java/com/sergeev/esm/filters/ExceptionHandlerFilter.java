package com.sergeev.esm.filters;

import com.sergeev.esm.exception.AuthenticateException;
import com.sergeev.esm.exception.RestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.CharEncoding.UTF_8;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The type Exception handler filter.
 */
@Component
@Order(value = HIGHEST_PRECEDENCE)
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            RestException restException = new AuthenticateException("Exception.notFound",
                    new ObjectError(request.toString(), "Exception.userWithNameFounded"));
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(UTF_8);
            response.getWriter().write(convertObjectToJson(restException));
        }
    }

    /**
     * Convert object to json string.
     *
     * @param object the object
     * @return the string
     * @throws JsonProcessingException the json processing exception
     */
    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}


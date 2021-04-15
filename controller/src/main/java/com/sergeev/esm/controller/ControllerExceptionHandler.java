package com.sergeev.esm.controller;


import com.sergeev.esm.exception.RestException;
import com.sergeev.esm.util.RestMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Controller exception handler.
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private static final String UNEXPECTED_ERROR = "Exception.unexpected";
    private static final String INVALID_DATA_ERROR = "Exception.invalid";
    private static final String INVALID_AUTHORITIES = "Exception.authorities";
    private static final String INVALID_ROLE = "Exception.role";
    private static final String INVALID_ID_ERROR = "Exception.invalidFormatId";

    private final MessageSource messageSource;

    /**
     * Handle illegal argument response entity.
     *
     * @param ex     the ex
     * @param locale the locale
     * @return the response entity
     */
    @ExceptionHandler(RestException.class)
    public ResponseEntity<RestMessage> handleIllegalArgument(RestException ex, Locale locale) {
        String reasonMessage = messageSource.getMessage(ex.getReason(), null, locale);
        ObjectError objectError = ex.getDescription();
        String errorMessage = messageSource.getMessage(objectError.getDefaultMessage(), null, locale);
        String result = String.format(errorMessage, objectError.getObjectName());
        return new ResponseEntity<>
                (new RestMessage(reasonMessage, Collections.singletonList(result), ex.getErrorCode()), ex.getHttpStatus());
    }

    /**
     * Handle argument not valid exception response entity.
     *
     * @param ex     the ex
     * @param locale the locale
     * @return the response entity
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<RestMessage> handleArgumentNotValidException(MethodArgumentNotValidException ex, Locale locale) {
        BindingResult result = ex.getBindingResult();
        List<String> errorMessages = result.getAllErrors()
                .stream()
                .map(objectError -> messageSource.getMessage(objectError, locale))
                .collect(Collectors.toList());
        String reasonMessage = messageSource.getMessage(INVALID_DATA_ERROR, null, locale);
        return new ResponseEntity<>(new RestMessage(reasonMessage, errorMessages,
                RestMessage.ERROR_CODE_OF_NOT_VALID_ARGUMENTS), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RestMessage> handleTypeNotValidException(MethodArgumentTypeMismatchException ex, Locale locale) {
        String reasonMessage = messageSource.getMessage(INVALID_DATA_ERROR, null, locale);
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(messageSource.getMessage(INVALID_ID_ERROR, null, locale));
        return new ResponseEntity<>(new RestMessage(reasonMessage, errorMessages,
                RestMessage.ERROR_CODE_OF_NOT_VALID_ARGUMENTS), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestMessage> handleAccessDeniedException(AccessDeniedException ex, Locale locale) {
        String reasonMessage = messageSource.getMessage(INVALID_AUTHORITIES, null, locale);
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(messageSource.getMessage(INVALID_ROLE, null, locale));
        return new ResponseEntity<>(new RestMessage(reasonMessage, errorMessages,
                RestMessage.ERROR_CODE_FORBIDDEN), HttpStatus.CONFLICT);
    }

    /**
     * Handle exceptions response entity.
     *
     * @param ex     the ex
     * @param locale the locale
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestMessage> handleExceptions(Exception ex, Locale locale) {
        String errorMessage;
        Throwable cause, resultCause = ex;
        while ((cause = resultCause.getCause()) != null && resultCause != cause) {
            resultCause = cause;
        }
        if (resultCause instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) resultCause)
                    .getConstraintViolations();
            List<String> errorMessages = new ArrayList<>();
            constraintViolations.forEach(constraintViolation -> errorMessages.add(constraintViolation.getMessage()));
            String reasonMessage = messageSource.getMessage(INVALID_DATA_ERROR, null, locale);
            return new ResponseEntity<>(new RestMessage(reasonMessage, errorMessages,
                    RestMessage.ERROR_CODE_OF_CONFLICT_ARGUMENTS),
                    HttpStatus.CONFLICT);
        } else {
            errorMessage = messageSource.getMessage(UNEXPECTED_ERROR, null, locale);
            ex.printStackTrace();
            return new ResponseEntity<>(new RestMessage(errorMessage, RestMessage.ERROR_CODE_OF_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


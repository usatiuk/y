package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.ErrorTo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage), HttpStatus.BAD_REQUEST.value()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(List.of(ex.getMessage()), HttpStatus.UNAUTHORIZED.value()),
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(List.of(ex.getMessage()), HttpStatus.FORBIDDEN.value()),
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(List.of(ex.getMessage()), HttpStatus.UNAUTHORIZED.value()),
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(List.of(Objects.requireNonNullElse(ex.getReason(), ex.getStatusCode().toString())), ex.getStatusCode().value()),
                new HttpHeaders(), ex.getStatusCode(), request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(List.of("Error"), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}

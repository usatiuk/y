package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.ErrorTo;
import com.usatiuk.tjv.y.server.service.exceptions.BadInputException;
import com.usatiuk.tjv.y.server.service.exceptions.ConflictException;
import com.usatiuk.tjv.y.server.service.exceptions.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
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

    @ExceptionHandler(TransactionSystemException.class)
    protected ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException ex, WebRequest request) {
        if (ex.getRootCause() instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) ex.getRootCause(), request);
        } else {
            return handleExceptionInternal(ex,
                    new ErrorTo(List.of("Error"), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        if (ex.getRootCause() instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) ex.getRootCause(), request);
        } else {
            return handleExceptionInternal(ex,
                    new ErrorTo(List.of("Something is wrong with your request"), HttpStatus.BAD_REQUEST.value()),
                    new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        }
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

    @ExceptionHandler(BadInputException.class)
    protected ResponseEntity<Object> handleBadInputException(BadInputException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST.value()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(ConflictException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(List.of(ex.getMessage()), HttpStatus.CONFLICT.value()),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(List.of(ex.getMessage()), HttpStatus.NOT_FOUND.value()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(List.of(ex.getMessage()), HttpStatus.NOT_FOUND.value()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    protected ResponseEntity<Object> handleEJpaObjectRetrievalFailureException(JpaObjectRetrievalFailureException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(List.of(ex.getMessage()), HttpStatus.NOT_FOUND.value()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
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

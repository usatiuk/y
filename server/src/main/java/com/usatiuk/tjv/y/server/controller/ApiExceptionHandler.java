package com.usatiuk.tjv.y.server.controller;

import com.usatiuk.tjv.y.server.dto.ErrorTo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ErrorTo(ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage), HttpStatus.BAD_REQUEST.value()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}

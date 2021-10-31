package com.evoluum.controller.exception;

import com.evoluum.exception.EvoluumException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(EvoluumException.class)
    public ResponseEntity<String> exceptionHandler(HttpServletRequest request, EvoluumException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getErrorMessage());
    }
}

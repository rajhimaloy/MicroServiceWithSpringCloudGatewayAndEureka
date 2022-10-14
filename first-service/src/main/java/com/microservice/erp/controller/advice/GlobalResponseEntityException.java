package com.microservice.erp.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@ControllerAdvice
public class GlobalResponseEntityException extends ResponseEntityExceptionHandler {

    private static Logger LOG = LoggerFactory.getLogger(GlobalResponseEntityException.class);

    @Override @SuppressWarnings("Duplicates")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex
            , HttpHeaders headers
            , HttpStatus status
            , WebRequest request) {
        //
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date().getTime());
        body.put("status", status.value());
        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);
        LOG.error(body.toString());
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllKindOfExceptions(
            Exception ex, WebRequest request) {
        //
        List<String> error = new ArrayList<>();
        error.add(ex.getLocalizedMessage());
        LOG.error(ex.getMessage());
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

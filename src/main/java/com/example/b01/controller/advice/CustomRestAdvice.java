package com.example.b01.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handBindException(BindException e){

        log.error(e);
        Map<String, String> errorMap = new HashMap<>();

        if(e.hasErrors()){
            e.getBindingResult().getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }

        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String,String>> handleFKException(Exception e){
        log.info(e);
        Map<String ,String> errorMap = new HashMap<>();
        errorMap.put("time",""+ System.currentTimeMillis());
        errorMap.put("msg","constraint fails");
        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String,String>> handleNoSuchException(Exception e) {
        log.info(e);
        Map<String ,String> errorMap = new HashMap<>();
        errorMap.put("time",""+ System.currentTimeMillis());
        errorMap.put("msg"," No Such Element Exception");
        return ResponseEntity.badRequest().body(errorMap);
    }

    }


package com.assignment.fluxkart.Controller;

import com.assignment.fluxkart.Exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity resourceNotFoundException(ResourceNotFoundException ex){
        Map<String,String>map = new HashMap<>();
        map.put(ex.getTitle(),ex.getContent());
        return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity globalExceptionHandle(Exception ex){
        ex.printStackTrace();
        Map<String,String>map = new HashMap<>();
        map.put("message", ex.getMessage());
        return new ResponseEntity(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

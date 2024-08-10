package com.example.lms.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionHandlers {


    @ExceptionHandler
    public  ResponseEntity<GlobalErrorResponse> handleException(GlobalNotFoundException e){
        GlobalErrorResponse err=new GlobalErrorResponse();
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setMessage(e.getMessage());
        err.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
    };

    @ExceptionHandler
    public ResponseEntity<GlobalErrorResponse> handleException(Exception e){
        GlobalErrorResponse err=new GlobalErrorResponse();
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setMessage(e.getMessage());
        err.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }
}

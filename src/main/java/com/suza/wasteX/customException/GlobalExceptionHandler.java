package com.suza.wasteX.customException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleProjectNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<Object> handleJwtException(JwtExpiredException ex) {
        Map<String ,Object> errorResponse = new HashMap<>();
        errorResponse.put("Header", ex.getHeader());
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("status", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }




}

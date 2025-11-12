package pl.fishingwear.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(basePackages = "pl.fishingwear.cart.web")
public class CartApiExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(
                Map.of("message", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}

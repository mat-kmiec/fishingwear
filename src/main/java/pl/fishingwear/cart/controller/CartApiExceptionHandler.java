package pl.fishingwear.cart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.fishingwear.cart.exception.CartNotFoundException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice(basePackages = "pl.fishingwear.cart")
public class CartApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CartApiExceptionHandler.class);

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCartNotFound(CartNotFoundException ex) {
        log.warn("Cart not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(ex.getMessage(), "CART_NOT_FOUND", Instant.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse("Internal server error", "INTERNAL_ERROR", Instant.now()));
    }

    public record ApiErrorResponse(String message, String errorCode, Instant timestamp) {}
}

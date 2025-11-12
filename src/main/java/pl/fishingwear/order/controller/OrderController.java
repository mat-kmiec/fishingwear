package pl.fishingwear.order.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.fishingwear.order.dto.CheckoutFormDto;
import pl.fishingwear.order.service.OrderService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@Valid @RequestBody CheckoutFormDto checkoutData) {
        try {
            Long orderId = orderService.placeOrder(checkoutData);
            return ResponseEntity.ok(Map.of("orderId", orderId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

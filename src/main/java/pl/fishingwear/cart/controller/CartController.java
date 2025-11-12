package pl.fishingwear.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.fishingwear.cart.dto.AddItemToCartDto;
import pl.fishingwear.cart.dto.CartViewDto;
import pl.fishingwear.cart.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartViewDto> addItemToCart(@Valid @RequestBody AddItemToCartDto itemDto) {
        CartViewDto updatedCart = cartService.addItemToCart(itemDto);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping
    public ResponseEntity<CartViewDto> getCart() {
        return ResponseEntity.ok(cartService.getCurrentCartView());
    }
}
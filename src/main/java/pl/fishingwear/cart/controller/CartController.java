package pl.fishingwear.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    @GetMapping
    public ResponseEntity<CartViewDto> getCart() {
        return ResponseEntity.ok(cartService.getCurrentCartView());
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        cartService.removeItemFromCart(itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartViewDto> updateQuantity(
            @PathVariable Long itemId,
            @RequestParam @Min(1) int quantity
    ) {
        CartViewDto updatedCart = cartService.updateItemQuantity(itemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }
}

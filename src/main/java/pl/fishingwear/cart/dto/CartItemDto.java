package pl.fishingwear.cart.dto;

import java.math.BigDecimal;

public record CartItemDto(
        Long cartItemId,
        Long productVariantId,
        String productName,
        String productSlug,
        String imageUrl,
        String size,
        String color,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {}

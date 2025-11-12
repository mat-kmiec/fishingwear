package pl.fishingwear.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartViewDto(
        List<CartItemDto> items,
        int totalItemsCount,     
        BigDecimal subtotalPrice
) {}

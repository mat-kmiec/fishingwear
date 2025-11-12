package pl.fishingwear.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddItemToCartDto(
        @NotNull
        Long productVariantId,

        @NotNull
        @Min(1)
        Integer quantity
) {}

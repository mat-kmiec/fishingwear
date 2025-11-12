package pl.fishingwear.order.dto;

import java.math.BigDecimal;

public record OrderConfirmationDto(
        Long orderId,
        BigDecimal subtotal,
        BigDecimal shippingCost,
        BigDecimal total,
        String paymentMethod
) {}

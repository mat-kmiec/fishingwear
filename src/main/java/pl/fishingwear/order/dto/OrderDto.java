package pl.fishingwear.order.dto;

import pl.fishingwear.order.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto (
        Long orderId,
        BigDecimal total,
        OrderStatus status,
        LocalDateTime createdAt
) {}

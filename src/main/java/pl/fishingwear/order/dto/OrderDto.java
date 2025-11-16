package pl.fishingwear.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto (
        Long orderId,
        BigDecimal total,
        String status,
        LocalDateTime createdAt
) {}

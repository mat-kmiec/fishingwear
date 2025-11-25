package pl.fishingwear.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class AdminOrderItemDto {
    private String productName;
    private String sku;
    private String variantDetails;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
package pl.fishingwear.product.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductVariantDto {
    private Long colorId;
    private Long sizeId;
    private BigDecimal price;
    private int stockQuantity;
    private BigDecimal discountPrice;

    public boolean isAvailable() {
        return stockQuantity > 0;
    }
}

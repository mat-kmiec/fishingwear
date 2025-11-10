package pl.fishingwear.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
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

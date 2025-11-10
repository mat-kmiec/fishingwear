package pl.fishingwear.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private String details;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String slug;
    private List<String> images;
    private List<ProductVariantDto> variants;
    private List<String> categories;
}
package pl.fishingwear.product.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String details;
    private BigDecimal price;
    private String slug;
    private List<ColorDto> colors;
    private List<SizeDto> sizes;
    private List<ProductVariantDto> variants;
    private List<ProductImageDto> images;
    private String mainImageUrl;

    public String getFormattedPrice() {
        if (price == null) return "";
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pl", "PL"));
        return nf.format(price);
    }
}

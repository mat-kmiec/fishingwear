package pl.fishingwear.product.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String details;
    private BigDecimal price;
    private List<ColorDto> colors;
    private List<SizeDto> sizes;
    private List<ProductVariantDto> variants;

    public String getFormattedPrice() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pl", "PL"));
        return nf.format(price);
    }

}

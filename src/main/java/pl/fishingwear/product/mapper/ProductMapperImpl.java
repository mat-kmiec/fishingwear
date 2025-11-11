package pl.fishingwear.product.mapper;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import pl.fishingwear.product.dto.*;
import pl.fishingwear.product.model.Color;
import pl.fishingwear.product.model.Product;
import pl.fishingwear.product.model.ProductVariant;
import pl.fishingwear.product.model.Size;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toDto(@NonNull Product product) {
        var dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDetails(product.getDetails());
        dto.setColors(mapColors(product));
        dto.setSlug(product.getSlug());
        dto.setSizes(mapSizes(product));
        dto.setVariants(mapVariants(product));
        dto.setImages(mapImages(product));
        return dto;
    }

    private List<ColorDto> mapColors(Product product) {
        return product.getVariants().stream()
                .map(ProductVariant::getColor)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Color::getId, c -> c, (a, b) -> a))
                .values()
                .stream()
                .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
                .map(c -> new ColorDto(c.getId(), c.getName(), c.getHexCode()))
                .toList();
    }

    private List<SizeDto> mapSizes(Product product) {
        return product.getVariants().stream()
                .map(ProductVariant::getSize)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Size::getId, s -> s, (a, b) -> a))
                .values()
                .stream()
                .sorted(Comparator.comparing(Size::getId))
                .map(s -> new SizeDto(s.getId(), s.getName(), s.getDescription()))
                .toList();
    }

    private List<ProductVariantDto> mapVariants(Product product) {
        return product.getVariants().stream()
                .map(this::mapVariant)
                .toList();
    }

    private ProductVariantDto mapVariant(ProductVariant v) {
        var dto = new ProductVariantDto();
        dto.setColorId(v.getColor() != null ? v.getColor().getId() : null);
        dto.setSizeId(v.getSize() != null ? v.getSize().getId() : null);
        dto.setPrice(v.getPrice());
        dto.setDiscountPrice(v.getDiscountPrice());
        dto.setStockQuantity(v.getQuantity());
        return dto;
    }

    private List<ProductImageDto> mapImages(Product product) {
        return product.getImages().stream()
                .map(img -> new ProductImageDto(img.getId(), img.getImageUrl(), img.getIsMain()))
                .toList();
    }
}

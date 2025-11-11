package pl.fishingwear.product.mapper;

import org.springframework.security.core.parameters.P;
import pl.fishingwear.product.dto.*;
import pl.fishingwear.product.model.Color;
import pl.fishingwear.product.model.Product;
import pl.fishingwear.product.model.ProductVariant;
import pl.fishingwear.product.model.Size;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDetails(product.getDetails());

        Set<Color> colors = product.getVariants().stream()
                .map(ProductVariant::getColor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        dto.setColors(colors.stream().map(c -> {
            ColorDto colorDto = new ColorDto();
            colorDto.setId(c.getId());
            colorDto.setName(c.getName());
            colorDto.setHexCode(c.getHexCode());
            return colorDto;
        }).toList());

        Set<Size> sizes = product.getVariants().stream()
                .map(ProductVariant::getSize)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        dto.setSizes(sizes.stream().map(s -> {
            SizeDto sizeDto = new SizeDto();
            sizeDto.setId(s.getId());
            sizeDto.setName(s.getName());
            return sizeDto;
        }).toList());

        dto.setVariants(product.getVariants().stream().map(v -> {
            ProductVariantDto varDto = new ProductVariantDto();
            varDto.setColorId(v.getColor().getId());
            varDto.setSizeId(v.getSize().getId());
            varDto.setPrice(v.getPrice());
            varDto.setStockQuantity(v.getQuantity());
            varDto.setDiscountPrice(v.getDiscountPrice());
            return varDto;
        }).toList());

        dto.setImages(product.getImages().stream().map(img -> {
            ProductImageDto imgDto = new ProductImageDto();
            imgDto.setId(img.getId());
            imgDto.setUrl(img.getImageUrl());
            imgDto.setMainImage(img.getIsMain());
            return imgDto;
        }).toList());

        return dto;
    }
}

package pl.fishingwear.product.dto;

public record ProductImageDto(
        Long id,
        String url,
        boolean mainImage
) {}

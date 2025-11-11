package pl.fishingwear.product.mapper;

import pl.fishingwear.product.dto.ProductDto;
import pl.fishingwear.product.model.Product;

public interface ProductMapper {
    ProductDto toDto(Product product);
}
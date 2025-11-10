package pl.fishingwear.product.mapper;

import org.springframework.security.core.parameters.P;
import pl.fishingwear.product.dto.ProductDto;
import pl.fishingwear.product.model.Product;

public class ProductMapper {

    public static ProductDto toDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        return productDto;
    }
}

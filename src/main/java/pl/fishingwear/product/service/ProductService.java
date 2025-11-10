package pl.fishingwear.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.product.dto.ColorDto;
import pl.fishingwear.product.dto.ProductDto;
import pl.fishingwear.product.dto.SizeDto;
import pl.fishingwear.product.exception.ProductNotFoundException;
import pl.fishingwear.product.mapper.ProductMapper;
import pl.fishingwear.product.model.Product;
import pl.fishingwear.product.model.ProductVariant;
import pl.fishingwear.product.repository.ProductRepository;
import pl.fishingwear.product.repository.ProductVariantRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    @Transactional
    public ProductDto getProductBySlug(String slug){
        Product product = productRepository.findBySlug(slug).orElseThrow(ProductNotFoundException::new);

        ProductDto productDto = ProductMapper.toDto(product);
        var variants = productVariantRepository.findByProduct(product);
        var colors = variants.stream()
                .map(ProductVariant::getColor)
                .filter(Objects::nonNull)
                .distinct()
                .map(c -> new ColorDto(c.getId(),c.getName(), c.getHexCode()))
                .toList();

        productDto.setColors(colors);

        var sizes = variants.stream()
                .map(v -> v.getSize())
                .filter(Objects::nonNull)
                .distinct()
                .map(s -> new SizeDto(s.getId() ,s.getName(), s.getDescription()))
                .toList();

        productDto.setSizes(sizes);

        return productDto;


    }
}

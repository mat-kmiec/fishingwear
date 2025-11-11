package pl.fishingwear.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.product.dto.ProductDto;
import pl.fishingwear.product.exception.ProductNotFoundException;
import pl.fishingwear.product.mapper.ProductMapper;
import pl.fishingwear.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductDto getProductBySlug(String slug) {
        var product = productRepository.findBySlugWithVariantsAndImages(slug)
                .orElseThrow(ProductNotFoundException::new);

        return productMapper.toDto(product);
    }
}

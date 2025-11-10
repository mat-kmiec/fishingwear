package pl.fishingwear.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fishingwear.product.dto.ProductResponseDto;
import pl.fishingwear.product.dto.ProductVariantDto;
import pl.fishingwear.product.model.Category;
import pl.fishingwear.product.model.Product;
import pl.fishingwear.product.model.ProductImage;
import pl.fishingwear.product.repository.ProductRepository;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Produkt nie znaleziony"));

        return mapToDto(product);
    }

    private ProductResponseDto mapToDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .details(product.getDetails())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .slug(product.getSlug())
                .images(product.getImages().stream()
                        .sorted(Comparator.comparingInt(ProductImage::getSortOrder))
                        .map(ProductImage::getImageUrl)
                        .toList())
                .categories(product.getCategories().stream()
                        .map(Category::getName)
                        .toList())
                .variants(product.getVariants().stream()
                        .map(variant -> ProductVariantDto.builder()
                                .sku(variant.getSku())
                                .size(variant.getSize() != null ? variant.getSize().getName() : null)
                                .color(variant.getColor() != null ? variant.getColor().getName() : null)
                                .price(variant.getPrice())
                                .quantity(variant.getQuantity())
                                .build())
                        .toList())
                .build();
    }
}
package pl.fishingwear.product.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fishingwear.product.dto.ProductDto;
import pl.fishingwear.product.exception.ProductNotFoundException;
import pl.fishingwear.product.mapper.ProductMapper;
import pl.fishingwear.product.model.Product;
import pl.fishingwear.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    public Page<ProductDto> getFilteredProducts(
            int page, int size, String sortBy, String sortDir,
            String categorySlug, List<String> colors, List<String> sizes,
            String search, BigDecimal minPrice, BigDecimal maxPrice
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categorySlug != null && !categorySlug.isEmpty()) {
                Join<Object, Object> categoryJoin = root.join("categories");
                predicates.add(cb.equal(categoryJoin.get("slug"), categorySlug));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (search != null && !search.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
            }

            if (colors != null && !colors.isEmpty()) {
                Join<Object, Object> variantJoin = root.join("variants");
                Join<Object, Object> colorJoin = variantJoin.join("color");
                predicates.add(cb.lower(colorJoin.get("name")).in(
                        colors.stream().map(String::toLowerCase).toList()
                ));
            }

            if (sizes != null && !sizes.isEmpty()) {
                Join<Object, Object> variantJoin = root.join("variants");
                Join<Object, Object> sizeJoin = variantJoin.join("size");
                predicates.add(cb.lower(sizeJoin.get("name")).in(
                        sizes.stream().map(String::toLowerCase).toList()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> products = productRepository.findAll(spec, pageable);
        return products.map(productMapper::toDto);
    }
}

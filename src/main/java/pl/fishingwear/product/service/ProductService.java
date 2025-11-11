package pl.fishingwear.product.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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

    private static final List<String> ALLOWED_SORT_FIELDS = List.of("name", "price", "createdAt");

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductDto getProductBySlug(String slug) {
        var product = productRepository.findBySlugWithVariantsAndImages(slug)
                .orElseThrow(ProductNotFoundException::new);
        return productMapper.toDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getFilteredProducts(
            int page,
            int size,
            String sortBy,
            String sortDir,
            String categorySlug,
            List<String> colors,
            List<String> sizes,
            String search,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "name";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Specification<Product> spec = buildSpecification(categorySlug, colors, sizes, search, minPrice, maxPrice);

        Page<Product> products = productRepository.findAll(spec, pageable);
        return products.map(productMapper::toDto);
    }

    private Specification<Product> buildSpecification(
            String categorySlug,
            List<String> colors,
            List<String> sizes,
            String search,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (categorySlug != null && !categorySlug.isEmpty()) {
                predicates.add(cb.equal(root.join("categories").get("slug"), categorySlug));
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

            Join<Object, Object> variantJoin = null;
            if ((colors != null && !colors.isEmpty()) || (sizes != null && !sizes.isEmpty())) {
                variantJoin = root.join("variants");
            }

            if (colors != null && !colors.isEmpty() && variantJoin != null) {
                predicates.add(cb.lower(variantJoin.join("color").get("name"))
                        .in(colors.stream().map(String::toLowerCase).toList()));
            }

            if (sizes != null && !sizes.isEmpty() && variantJoin != null) {
                predicates.add(cb.lower(variantJoin.join("size").get("name"))
                        .in(sizes.stream().map(String::toLowerCase).toList()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

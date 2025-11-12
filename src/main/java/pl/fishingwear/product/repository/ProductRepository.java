package pl.fishingwear.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.fishingwear.product.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.variants v " +
            "LEFT JOIN FETCH v.color " +
            "LEFT JOIN FETCH v.size " +
            "LEFT JOIN FETCH p.images " +
            "WHERE p.slug = :slug")
    Optional<Product> findBySlugWithVariantsAndImages(@Param("slug") String slug);
}

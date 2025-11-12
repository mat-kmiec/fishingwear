package pl.fishingwear.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.fishingwear.product.model.ProductVariant;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    @Query("SELECT pv FROM ProductVariant pv " +
            "LEFT JOIN FETCH pv.product p " +
            "LEFT JOIN FETCH p.images " +
            "LEFT JOIN FETCH pv.size " +
            "LEFT JOIN FETCH pv.color " +
            "WHERE pv.id = :id")
    Optional<ProductVariant> findByIdWithDetails(@Param("id") Long id);
}

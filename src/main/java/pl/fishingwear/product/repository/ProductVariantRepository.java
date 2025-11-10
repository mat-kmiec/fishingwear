package pl.fishingwear.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fishingwear.product.model.Product;
import pl.fishingwear.product.model.ProductVariant;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    List<ProductVariant> findByProduct(Product product);
}

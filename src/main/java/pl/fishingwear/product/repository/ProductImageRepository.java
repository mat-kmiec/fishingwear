package pl.fishingwear.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fishingwear.product.model.Product;
import pl.fishingwear.product.model.ProductImage;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
    List<ProductImage> findByProduct(Product product);
}
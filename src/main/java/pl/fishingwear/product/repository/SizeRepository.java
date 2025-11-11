package pl.fishingwear.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fishingwear.product.model.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
}
package pl.fishingwear.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fishingwear.product.model.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
}